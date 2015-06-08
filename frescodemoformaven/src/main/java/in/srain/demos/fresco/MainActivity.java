package in.srain.demos.fresco;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.AndroidPredicates;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Dictionary;

import dalvik.system.DexFile;

public class MainActivity extends Activity {
    //Uri aniImageUri = Uri.parse("https://camo.githubusercontent.com/588a2ef2cdcfb6c71e88437df486226dd15605b3/687474703a2f2f737261696e2d6769746875622e71696e6975646e2e636f6d2f756c7472612d7074722f73746f72652d686f7573652d737472696e672d61727261792e676966");
    //Uri proImageUri = Uri.parse("http://192.168.1.135:8080/image/king_pro.jpg");
    //Uri proImageUri = Uri.parse("http://192.168.1.135:8080/image/DSC_0097_pro.JPG");
    //Uri proImageUri = Uri.parse("http://192.168.1.135:8080/image/DSC_0097_HD1080_pro.jpg");
    //Uri proImageUri = Uri.parse("http://192.168.1.135:8080/image/DSC_final.JPG");
    //Uri proImageUri = Uri.parse("http://192.168.1.135:8080/image/DSC_final_pro.JPG");
    final Uri proImageUri =Uri.parse("http://pooyak.com/p/progjpeg/jpegload.cgi?o=1"); // the best image to show loading progressive.
    final Uri lowResUri = Uri.parse("http://u4.tdimg.com/7/147/82/31804659546604080410941337579323207967.jpg");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(this.getResources())
                .setProgressBarImage(new ProgressBarDrawable())
                .setRoundingParams(RoundingParams.asCircle())
                .build();

        //final SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.logo_image);
        final SimpleDraweeView progressivePic = (SimpleDraweeView) findViewById(R.id.progressive);
        progressivePic.setHierarchy(gdh);
        Button buttonClearCache = (Button) findViewById(R.id.clearCache);
        Button buttonReload = (Button) findViewById(R.id.reload);
//        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast toast = Toast.makeText(getApplicationContext()," Clear the Cache !" ,Toast.LENGTH_SHORT);
//                toast.show();
//                renderProgressive(progressivePic, lowResUri);
//                Fresco.getImagePipeline().evictFromMemoryCache(proImageUri);
//
//            }
//        });

        buttonClearCache.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                renderProgressive(progressivePic, lowResUri);
                Fresco.getImagePipeline().evictFromMemoryCache(proImageUri);

                File cacheDir = getApplicationContext().getCacheDir();
                long num = 0;
                try {
                    num = clearCache(cacheDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("dddd", "clear cache size :  :" + FormetFileSize(num));
                Toast toast = Toast.makeText(getApplicationContext(),
                        " Clear the Cache size  :" +  FormetFileSize(num)/*+ Runtime.getRuntime().freeMemory()*/,
                        Toast.LENGTH_SHORT);

                toast.show();

            }
        });


        buttonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderProgressive(progressivePic, proImageUri);
            }

        });
        //simpleDraweeView.setImageURI(lowResUri);

        renderProgressive(progressivePic, proImageUri);
    }

    void renderProgressive( SimpleDraweeView view, Uri uri) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                        //.setUri(uri)
                .setOldController(view.getController())

                .build();
        view.setController(controller);

    }
    /*
        delete the file only ,remain the dictionary.
     */
    public long clearCache(File file) throws Exception{
        long count = 0;
        if (file.exists() && file.isDirectory()){
            File[] filelist = file.listFiles();
            for (int i = 0; i < filelist.length; ++i) {
                count += this.clearCache(filelist[i]);
                file.getFreeSpace();
            }
        } else {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                count += fis.available();
                file.delete();
            }
        }
        return count ;
    }
    private static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
