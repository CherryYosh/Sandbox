#version 130

uniform mat4 projection;
uniform mat4 modelview;

in vec3 in_Position;

out vec4 color;

void main(){
	
	gl_Position = projection * modelview * vec4(in_Position, 1.0);
        //gl_Position = ftransform();
}