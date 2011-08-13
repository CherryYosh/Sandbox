#version 140

out vec4 pixelColor;

in vec2 tex;

void main(){
	pixelColor = vec4(tex.y, 1, tex.x,1); //texture2D(tex, texCoord.st);
}