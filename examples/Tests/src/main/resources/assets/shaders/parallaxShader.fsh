#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
varying vec2 t0c, t1c;
uniform sampler2D u_texture0;
uniform sampler2D u_texture1;

void main() {
	vec4 texel0, texel1;  
	texel0 = texture2D(u_texture0, t0c);
	texel1 = texture2D(u_texture1, t1c);
	
	gl_FragColor = texel0;
	gl_FragColor = vec4(texel1.a) * texel1 + vec4(1.0 - texel1.a) * gl_FragColor;
	

}
