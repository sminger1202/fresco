/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook.fresco.sample.adapters;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.fresco.sample.Drawables;
import com.facebook.fresco.sample.MyPostprocessor;
import com.facebook.fresco.sample.instrumentation.InstrumentedDraweeView;
import com.facebook.fresco.sample.instrumentation.PerfListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/** Populate the list view with images using the Fresco image pipeline. */
public class FrescoAdapter extends ImageListAdapter<InstrumentedDraweeView> {

  public FrescoAdapter(
      Context context,
      int resourceId,
      PerfListener perfListener,
      ImagePipelineConfig imagePipelineConfig) {
    super(context, resourceId, perfListener);
    Fresco.initialize(context, imagePipelineConfig);
  }

  @Override
  protected Class<InstrumentedDraweeView> getViewClass() {
    return InstrumentedDraweeView.class;
  }

  protected InstrumentedDraweeView createView() {
    GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(getContext().getResources())
        .setPlaceholderImage(Drawables.sPlaceholderDrawable)
        .setFailureImage(Drawables.sErrorDrawable)
        .setRoundingParams(RoundingParams.asCircle())
        .setProgressBarImage(new ProgressBarDrawable())
        .build();
    return new InstrumentedDraweeView(getContext(), gdh);
  }

    Postprocessor redMeshPostprocessor = new MyPostprocessor();


    protected void bind(final InstrumentedDraweeView view, String uri) {
    ImageRequest imageRequest =
        ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                .setAutoRotateEnabled(true)// modify by sminger , note : may it contained in listview,so not work
                //.setPostprocessor(redMeshPostprocessor)// modify by sminger
                .setResizeOptions(
                        new ResizeOptions(view.getLayoutParams().width, view.getLayoutParams().height))
                .setProgressiveRenderingEnabled(true)
            .build();
        /*
        modify by sminger to verify the progressive loading image
         */
        Uri lowResUri = Uri.parse("http://u4.tdimg.com/7/147/82/31804659546604080410941337579323207967.jpg");
        //Uri heightResUri = Uri.parse("http://g1.ykimg.com/0516000051B6F2FA67583905D3081E0A");
        Uri heightResUri = Uri.parse("http://192.168.1.135:8080/image/king_pro.jpg");
        //Uri heightResUri = Uri.parse("http://192.168.1.135:8080/image/DSC_2237_pro.jpg");
        //Uri heightResUri = Uri.parse("https://img.okezone.com//content/2015/03/13/20/1117973/dirjen-pajak-blunder-soal-pajak-jalan-tol-Hhkw9dov48.jpg");
        //Uri heightResUri = Uri.parse("http://pooyak.com/p/progjpeg/jpegload.cgi?o=1");

        //Uri heightResUri = Uri.parse("http://192.168.1.135:8080/image/DSC_0097_pro.JPG");
        ImageRequest myImageRequest = ImageRequestBuilder.newBuilderWithSource(heightResUri)
                .setProgressiveRenderingEnabled(true)
                .build();

    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
            //.setLowResImageRequest(ImageRequest.fromUri(lowResUri))       //modify by sminger
            //.setImageRequest(imageRequest.fromUri(heightResUri))          //modify by sminger
            //.setImageRequest(myImageRequest)                                //modify bs sminger
            .setImageRequest(imageRequest)// default configuration
            .setOldController(view.getController())
            .setControllerListener(view.getListener())
            .setAutoPlayAnimations(true)
            .setTapToRetryEnabled(true)// modify by sminger
            .build();
    view.setController(draweeController);
  }

  @Override
  public void shutDown() {
    super.clear();
    Fresco.shutDown();
  }
}
