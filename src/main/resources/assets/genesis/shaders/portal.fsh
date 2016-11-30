uniform sampler2D tex;
uniform sampler2D overlay;
uniform float dx;
uniform float dy;
uniform float cx;
uniform float cy;
uniform float rad;

#define OVERLAY 0.0

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
		float amnt = pow(r, 3.0);
		if (r != 0)
		{
		  ox /= r;
		  oy /= r;
		  r = pow(r, 1.0 / 3.0);
		}
		vec3 col = texture2D(tex, gl_FragCoord.xy * mult).rgb * amnt;
        col += texture2D(tex, vec2(cx + ox * r, cy + oy * r) * mult).rgb * (1.0 - amnt);
        //vec3 col = texture2D(tex, vec2(cx + ox * r, cy + oy * r) * mult).rgb;
		if (OVERLAY > 0.0) col += texture2D(overlay, vec2(ox + 1.0, oy + 1.0) * mult * 0.5).rgb * OVERLAY;
		gl_FragColor = vec4(col, 1.0);
	}
}