//constants from mod
uniform sampler2D tex;
uniform sampler2D overlay;
uniform float pi;
//variables
uniform float dx;
uniform float dy;
uniform float cx;
uniform float cy;
uniform float rad;

#define OVERLAY 0.0
#define INSIDE 0.25
#define HOLE 0.25

void main()
{
    float ox = gl_FragCoord.x - cx;
	float oy = gl_FragCoord.y - cy;
	float r = sqrt(ox * ox + oy * oy);
	if (rad == 0.0 || r > rad) discard;
	else
	{
        vec2 mult = vec2(dx, dy);
        r /= rad;
        if (r <= HOLE) gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        else
        {
            float m = r;
            r = (r - HOLE) / (1.0 - HOLE);
            m = r / m;
            ox *= m;
            oy *= m;
            gl_FragColor.rgb = texture2D(tex, vec2(cx + ox, cy + oy) * mult).rgb;
            gl_FragColor.a = 1.0 - pow(r, 3.0);
            if (r <= INSIDE) gl_FragColor.rgb *= pow(r / INSIDE, 3.0);
        }
	}
}