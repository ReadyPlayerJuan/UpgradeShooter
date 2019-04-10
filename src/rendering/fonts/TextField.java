package rendering.fonts;

import org.lwjgl.opengl.GL11;
import rendering.FrameBuffer;

public class TextField {
    public static final int TOP_LEFT = 0;
    public static final int CENTER = 1;

    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int TOP = 4;
    public static final int MIDDLE = 5;
    public static final int BOTTOM = 6;

    private TrueTypeFont font;
    private int width, height;

    private FrameBuffer frameBuffer;
    private int bufferAlign = CENTER;
    private int hAlign = LEFT;
    private int vAlign = TOP;

    private String prevText = "";

    public TextField(TrueTypeFont font, int maxWidth, int maxNumLines) {
        this.font = font;
        this.width = maxWidth;
        this.height = maxNumLines * font.getFontHeight();

        frameBuffer = new FrameBuffer(width, height);
        updateFrameBuffer("");
    }

    public void setText(String text) {
        if(isNewText(text)) {
            updateFrameBuffer(text);
            prevText = text;
        }
    }

    public void drawText(int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if(bufferAlign == CENTER) {
            frameBuffer.draw(x, y);
        } else if(bufferAlign == TOP_LEFT) {
            frameBuffer.draw(x+width/2, y-height/2);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void updateFrameBuffer(String text) {
        frameBuffer.bindFrameBuffer();
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glColor3f(1, 1, 1);

        float textX = 0, textY = 0;

        if(hAlign == LEFT)
            textX = 0;
        else if(hAlign == CENTER)
            textX = (width - font.getStringWidth(text))/2;
        else if(hAlign == RIGHT)
            textX = width - font.getStringWidth(text);

        if(vAlign == TOP)
            textY = height-font.getFontHeight();
        else if(vAlign == MIDDLE)
            textY = (height-font.getFontHeight())/2;
        else if(vAlign == BOTTOM)
            textY = 0;

        font.drawText(text, textX, textY);
        frameBuffer.unbindFrameBuffer();
    }

    private boolean isNewText(String text) {
        if(text.length() != prevText.length())
            return true;
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) != prevText.charAt(i))
                return true;
        }
        return false;
    }

    public void setBufferAlign(int alignment) {
        this.bufferAlign = alignment;
    }

    public void setTextAlign(int hAlign, int vAlign) {
        this.hAlign = hAlign;
        this.vAlign = vAlign;
    }
}
