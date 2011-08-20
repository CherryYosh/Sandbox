#version 140

in vec2 in_texCoord;
out vec4 pixelColor;

uniform sampler2D texture;

void main(){
        vec4 outColor = texture2D(texture, in_texCoord.st);
        if(outColor.rgb == vec3(1,0,128.0/255.0)) discard;
	pixelColor = vec4(outColor);
} 