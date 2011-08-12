#version 130

uniform mat4 projection;
uniform mat4 modelview;

in vec3 vertex;

out vec4 color;

void main(){
	
	gl_Position = vec4(100,100,0);
}