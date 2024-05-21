package examples;
/*******************************************************************************************
*
*   raylib [models] example - Cubicmap loading and drawing
*
*   This example has been created using raylib 1.8 (www.raylib.com)
*   raylib is licensed under an unmodified zlib/libpng license (View raylib.h for details)
*
*   Copyright (c) 2015 Ramon Santamaria (@raysan5)
*
********************************************************************************************/


import com.raylib.*;

import java.lang.foreign.MemorySegment;

import static com.raylib.Raylib.*;
import static com.raylib.Raylib.CameraMode.CAMERA_ORBITAL;
import static com.raylib.Raylib.CameraProjection.CAMERA_PERSPECTIVE;

public class CubicMap {

    public static void main(String []args){
        // Initialization
        //--------------------------------------------------------------------------------------
    final int screenWidth = 800;
    final int screenHeight = 450;

        initWindow(screenWidth, screenHeight, "raylib [models] example - cubesmap loading and drawing");

        // Define the camera to look into our 3d world
        //Camera3D camera = new Camera(new Vector3(16.0f, 14.0f, 16.0f), new Vector3(0.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f), 45.0f, 0);
        Camera3D camera = new Camera3D(new Vector3(18,16,18),
                new Vector3(),
                new Vector3(0,1,0),
                45, CAMERA_PERSPECTIVE);

        Image image = loadImage("resources/cubicmap.png");      // Load cubicmap image (RAM)
        Texture cubicmap = loadTextureFromImage(image);       // Convert image to texture to display (VRAM)

        Mesh mesh = genMeshCubicmap(image, new Vector3(1.0f,1.0f,1.0f));
        Model model = loadModelFromMesh(mesh);

        // NOTE: By default each cube is mapped to one part of texture atlas
        Texture texture = loadTexture("resources/cubicmap_atlas.png");    // Load map texture
        //model.materials().maps().texture(texture);             // Set map diffuse texture


        MemorySegment maps = com.raylib.jextract.Material.maps(com.raylib.jextract.Model.materials(model.memorySegment));
        MemorySegment matmap = com.raylib.jextract.MaterialMap.asSlice(maps, 0);
        com.raylib.jextract.MaterialMap.texture(matmap, texture.memorySegment);

        Vector3 mapPosition = new Vector3(-16.0f, 0.0f,-8.0f);          // Set model position

        unloadImage(image);     // Unload cubesmap image from RAM, already uploaded to VRAM



        setTargetFPS(60);                       // Set our game to run at 60 frames-per-second
        //--------------------------------------------------------------------------------------

        // Main game loop
        while (!windowShouldClose())            // Detect window close button or ESC key
        {
            // Update
            //----------------------------------------------------------------------------------
            updateCamera(camera, CAMERA_ORBITAL);              // Update camera
            //----------------------------------------------------------------------------------

            // Draw
            //----------------------------------------------------------------------------------
            beginDrawing();

            clearBackground(RAYWHITE);

            beginMode3D(camera);

            drawModel(model, mapPosition, 1.0f, WHITE);

            endMode3D();

            drawTextureEx(cubicmap, new Vector2(screenWidth - cubicmap.getWidth() * 4 - 20, 20), 0.0f, 4.0f, WHITE);
            drawRectangleLines(screenWidth - cubicmap.getWidth() * 4 - 20, 20, cubicmap.getWidth() * 4, cubicmap.getHeight() * 4, GREEN);

            drawText("cubicmap image used to", 658, 90, 10, GRAY);
            drawText("generate map 3d model", 658, 104, 10, GRAY);

            drawFPS(10, 10);

            endDrawing();
            //----------------------------------------------------------------------------------
        }

        // De-Initialization
        //--------------------------------------------------------------------------------------
        unloadTexture(cubicmap);    // Unload cubicmap texture
        unloadTexture(texture);     // Unload map texture
        unloadModel(model);         // Unload map model

        closeWindow();              // Close window and OpenGL context
        //--------------------------------------------------------------------------------------


    }
}