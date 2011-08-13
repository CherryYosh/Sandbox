#version 140

uniform mat4 projection;
uniform mat4 modelview;

in vec3 vertex;

in vec4 color;

void main(){
	
	gl_Position = vec4(vertex, 1.0);
}