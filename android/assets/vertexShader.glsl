/// <summary>
/// Basic lighting vertex shader.
/// </summary>


/// <summary>
/// Material source structure.
/// <summary>
struct MaterialSource
{
    vec3 Ambient;
    vec4 Diffuse;
    vec3 Specular;
    float Shininess;
    vec2 TextureOffset;
    vec2 TextureScale;
};


/// <summary>
/// Attributes.
/// <summary>
attribute vec3 Vertex;
attribute vec2 Uv;
attribute vec3 Normal;


/// <summary>
/// Uniform variables.
/// <summary>

///this next one needs to be changed to u_projTrans to work with libgdx i think
///uniform mat4 ProjectionMatrix;
uniform mat4 u_projTrans;
///^^^^^ new one
uniform mat4 ViewMatrix;
uniform mat4 ModelMatrix;
uniform vec3 ModelScale;

uniform MaterialSource Material;


/// <summary>
/// Varying variables.
/// <summary>
varying vec4 vWorldVertex;
varying vec3 vWorldNormal;
varying vec2 vUv;
varying vec3 vViewVec;


///lol i don't know
uniform sampler2d u_texture;


/// <summary>
/// Vertex shader entry.
/// <summary>
void main ()
{
    // Transform the vertex
    vWorldVertex = ModelMatrix * vec4(Vertex * ModelScale, 1.0);
    vec4 viewVertex = ViewMatrix * vWorldVertex;
    gl_Position = u_projTrans * viewVertex;

    // Setup the UV coordinates
    vUv = Material.TextureOffset + (Uv * Material.TextureScale);

    // Rotate normal
    vWorldNormal = normalize(mat3(ModelMatrix) * Normal);

    // Calculate view vector (for specular lighting)
    vViewVec = normalize(-viewVertex.xyz);
}

