package rendering.shaders.layered_color;

import rendering.Graphics;
import rendering.shaders.ShaderProgram;

public class LayeredColorShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/rendering/shaders/layered_color/layeredColorVertex.txt";
    private static final String FRAGMENT_FILE = "src/rendering/shaders/layered_color/layeredColorFragment.txt";

    private int location_allColors;

    public LayeredColorShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_allColors = super.getUniformLocation("allColors");
    }

    public void loadColorTheme(float[][] colorTheme) {
        float[] allColors = new float[Graphics.NUM_COLORS_PER_REGION * Graphics.NUM_COLOR_REGIONS * 3];
        for(int region = 0; region < Graphics.NUM_COLOR_REGIONS; region++) {
            for(int col = 0; col < colorTheme[region].length; col++) {
                allColors[region * Graphics.NUM_COLORS_PER_REGION * 3 + col] = colorTheme[region][col];
            }
        }
        super.loadVector3Array(location_allColors, allColors);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
