package in.srain.demos.fresco;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;

public class DemoApp extends Application {
    private  ImagePipelineConfig ImagePipelineConfig;
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";
    @Override
    public void onCreate() {
        super.onCreate();

        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {

            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                Log.i("ssss", "ddddddd  scanNumber :" + scanNumber);
                return scanNumber + 1;
            }

            @Override
            public QualityInfo getQualityInfo(int scanNumber) {
                Log.i("ssss", "dddddddd======getQualityInfo:"+ scanNumber);
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(pjpegConfig);//modify by sminger
        configureCaches(configBuilder, this);
        ImagePipelineConfig = configBuilder.build();

        // Fresco.initialize(this);

        Fresco.initialize(this, ImagePipelineConfig);
        Fresco.getImagePipeline();
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static void configureCaches(
            ImagePipelineConfig.Builder configBuilder,
            Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder()
                                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)
                                .build());
    }
}
