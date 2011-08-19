#version 140

in vec2 in_texCoord;
out vec4 pixelColor;

uniform sampler2D texture;

void main(){
	pixelColor = texture2D(texture, in_texCoord.st);
}