#version 140

uniform mat4 projection;
uniform mat4 modelview;

in vec3 vertex;
in vec2 texCoord;
out vec2 in_texCoord;

void main(){
        in_texCoord = texCoord;
	gl_Position = (projection * modelview) * vec4(vertex, 1.0);
}