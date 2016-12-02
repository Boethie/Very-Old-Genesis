uniform sampler2D tex;
uniform float dx;
uniform float dy;

void main()
{
    gl_FragColor = vec4(texture2D(tex, gl_FragCoord.xy * vec2(dx, dy)).rgb, 1.0);
}