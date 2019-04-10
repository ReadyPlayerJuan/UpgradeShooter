package rendering.fonts;

/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static rendering.fonts.IOUtil.ioResourceToByteBuffer;

/** STB Truetype demo. */
public final class TrueTypeFont {
    //private final String text;

    private int fontHeight;

    private float lineHeight;

    private boolean kerningEnabled = true;
    private boolean lineBBEnabled;

    private final ByteBuffer ttf;

    private final STBTTFontinfo info;

    private int texID;

    private int BITMAP_W = 512;
    private int BITMAP_H = 512;

    private final int ascent;
    private final int descent;
    private final int lineGap;

    private STBTTBakedChar.Buffer cdata;

    public TrueTypeFont(int fontHeight) {
        this.fontHeight = fontHeight;
        this.lineHeight = fontHeight;

        String t;

        int lc;


        try {
            ttf = ioResourceToByteBuffer("res/monofonto.ttf", 512 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        info = STBTTFontinfo.create();
        if (!stbtt_InitFont(info, ttf)) {
            throw new IllegalStateException("Failed to initialize font information.");
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pAscent  = stack.mallocInt(1);
            IntBuffer pDescent = stack.mallocInt(1);
            IntBuffer pLineGap = stack.mallocInt(1);

            stbtt_GetFontVMetrics(info, pAscent, pDescent, pLineGap);

            ascent = pAscent.get(0);
            descent = pDescent.get(0);
            lineGap = pLineGap.get(0);
        }

        init();
    }

    private void init() {
        texID = glGenTextures();
        STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(96);

        ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
        stbtt_BakeFontBitmap(ttf, fontHeight, bitmap, BITMAP_W, BITMAP_H, 32, cdata);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.cdata = cdata;
    }

    public void drawText(String text, float x, float y) {
        float scale = stbtt_ScaleForPixelHeight(info, fontHeight);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);

            FloatBuffer fx = stack.floats(0.0f);
            FloatBuffer fy = stack.floats(0.0f);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            int lineStart = 0;

            float lineY = 0.0f;

            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, texID);
            glBegin(GL_QUADS);
            for (int i = 0, to = text.length(); i < to; ) {
                i += getCP(text, to, i, pCodePoint);

                int cp = pCodePoint.get(0);
                if (cp == '\n') {
                    if (lineBBEnabled) {
                        glEnd();
                        renderLineBB(text, lineStart, i - 1, fy.get(0), scale);
                        glBegin(GL_QUADS);
                    }

                    fy.put(0, lineY = fy.get(0) + (ascent - descent + lineGap) * scale);
                    fx.put(0, 0.0f);

                    lineStart = i;
                    continue;
                } else if (cp < 32 || 128 <= cp) {
                    continue;
                }

                float cpX = fx.get(0);
                stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, cp - 32, fx, fy, q, true);
                fx.put(0, fx.get(0));
                if (kerningEnabled && i < to) {
                    getCP(text, to, i, pCodePoint);
                    fx.put(0, fx.get(0) + stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0)) * scale);
                }

                float
                        x0 = q.x0() + x,
                        x1 = q.x1() + x,
                        y0 = -q.y0() + y,
                        y1 = -q.y1() + y;

                glTexCoord2f(q.s0(), q.t0());
                glVertex2f(x0, y0);

                glTexCoord2f(q.s1(), q.t0());
                glVertex2f(x1, y0);

                glTexCoord2f(q.s1(), q.t1());
                glVertex2f(x1, y1);

                glTexCoord2f(q.s0(), q.t1());
                glVertex2f(x0, y1);
            }
            glEnd();
            glBindTexture(GL_TEXTURE_2D, 0);
            if (lineBBEnabled) {
                renderLineBB(text, lineStart, text.length(), lineY, scale);
            }
        }
    }

    private void renderLineBB(String text, int from, int to, float y, float scale) {
        glDisable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT, GL_LINE);
        //glColor3f(1.0f, 1.0f, 0.0f);

        float width = getStringWidth(info, text, from, to, fontHeight);
        y -= descent * scale;

        glBegin(GL_QUADS);
        glVertex2f(0.0f, y);
        glVertex2f(width, y);
        glVertex2f(width, y - fontHeight);
        glVertex2f(0.0f, y - fontHeight);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT, GL_FILL);
    }

    private float getStringWidth(STBTTFontinfo info, String text, int from, int to, int fontHeight) {
        int width = 0;

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint       = stack.mallocInt(1);
            IntBuffer pAdvancedWidth   = stack.mallocInt(1);
            IntBuffer pLeftSideBearing = stack.mallocInt(1);

            int i = from;
            while (i < to) {
                i += getCP(text, to, i, pCodePoint);
                int cp = pCodePoint.get(0);

                stbtt_GetCodepointHMetrics(info, cp, pAdvancedWidth, pLeftSideBearing);
                width += pAdvancedWidth.get(0);

                if (kerningEnabled && i < to) {
                    getCP(text, to, i, pCodePoint);
                    width += stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0));
                }
            }
        }

        return width * stbtt_ScaleForPixelHeight(info, fontHeight);
    }

    private static int getCP(String text, int to, int i, IntBuffer cpOut) {
        char c1 = text.charAt(i);
        if (Character.isHighSurrogate(c1) && i + 1 < to) {
            char c2 = text.charAt(i + 1);
            if (Character.isLowSurrogate(c2)) {
                cpOut.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        cpOut.put(0, c1);
        return 1;
    }

    public void cleanUp() {
        cdata.free();
    }
}