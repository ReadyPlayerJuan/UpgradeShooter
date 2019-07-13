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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void drawText(String text, float x, float y, int xScale) {
        float scale = stbtt_ScaleForPixelHeight(info, fontHeight);

        y -= (int)(fontHeight * 0.3);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);

            FloatBuffer fx = stack.floats(0.0f);
            FloatBuffer fy = stack.floats(0.0f);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            int lineStart = 0;

            float lineY = 0.0f;

            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, texID);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glBegin(GL_QUADS);
            for (int i = 0, to = text.length(); i < to; ) {
                i += getCP(text, to, i, pCodePoint);

                int cp = pCodePoint.get(0);
                if (cp == '\n') {
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
                    fx.put(0, fx.get(0) + xScale * stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0)) * scale);
                }

                float
                        x0 = q.x0() + x,
                        x1 = q.x1() + x,
                        y0 = q.y0() + y,
                        y1 = q.y1() + y;

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
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_BLEND);
        }
    }

    public float getStringWidth(String text) {
        return getStringWidth(text, 0, text.length());
    }

    private float getStringWidth(String text, int from, int to) {
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

    public float getStringHeight(String text) {
        int lc = 0;
        Matcher m = Pattern.compile("^.*$", Pattern.MULTILINE).matcher(text);
        while (m.find()) {
            lc++;
        }
        return fontHeight * lc;
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

    public int getFontHeight() {
        return fontHeight;
    }

    public void cleanUp() {
        cdata.free();
    }
}