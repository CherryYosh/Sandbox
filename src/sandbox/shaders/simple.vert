#version 140

in vec3 vertex;
in vec2 texCoord;
out vec2 tex;

void main(){
	
	gl_Position = vec4(vertex, 1.0);
        tex = texCoord;
}