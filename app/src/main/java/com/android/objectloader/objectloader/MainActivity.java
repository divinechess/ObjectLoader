package com.android.objectloader.objectloader;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {





        private GLSurfaceView glSurfaceView;
        private boolean rendererSet = false;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            glSurfaceView = new GLSurfaceView(this);

            final ActivityManager activityManager = (ActivityManager)
                    getSystemService(Context.ACTIVITY_SERVICE);

            final ConfigurationInfo configurationInfo =
                    activityManager.getDeviceConfigurationInfo();
            final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

            final boolean supportsEs2 =
                    configurationInfo.reqGlEsVersion >= 0x20000
                            || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                            &&(Build.FINGERPRINT.startsWith("generic")
                            || Build.FINGERPRINT.startsWith("unknown")
                            || Build.MODEL.contains("google_sdk")
                            || Build.MODEL.contains("Emulator")
                            || Build.MODEL.contains("Android SDK built for x86")));


            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);

            //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            //fab.setOnClickListener(new View.OnClickListener() {


            if (supportsEs2) {
                // Request an OpenGL ES 2.0 compatible context.
                glSurfaceView.setEGLContextClientVersion(2);

                // Assign our renderer.
                glSurfaceView.setRenderer(new GroupObject (this));
                rendererSet = true;
            } else {


                //version_toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                //  version_toast.LENGTH_LONG).show();
                return;
            }

            setContentView(glSurfaceView);

        }


        @Override
        protected void onPause() {
            super.onPause();

            if (rendererSet) {
                glSurfaceView.onPause();
            }
        }

        @Override
        protected void onResume() {
            super.onResume();

            if (rendererSet) {
                glSurfaceView.onResume();
            }
        }






        //    @Override
        //   public void onClick(View view) {
        //       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //              .setAction("Action", null).show();
        //   }
        // });




        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_first_open_glproject, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
