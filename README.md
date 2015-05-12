# BitmapAdapterhttps://github.com/bitcores/BitmapAdapter/edit/master/README.md#fullscreen
An Android class for handling bitmap loading from file or network/Internet

This can be called using a single method

getBitmap(String path, Integer type, Integer method, ImageView imageView, BitmapResponse response)

path: path to the file, filesystem path or url string

type: url or file request, TYPE_URL or TYPE_FILE

method: return the bitmap or set the bitmap to imageview, METHOD_GET or METHOD_SET

imageView: the imageview to set the bitmap to or null

response: the BitmapResponse callback or null


Set up a BitmapResponse callback like so

BitmapAdapter.BitmapResponse returnResponse = new BitmapAdapter.BitmapResponse() {

  @Override
  
  public void bitmapReturn(Bitmap bitmap) {
  
    Log.i(TAG, "Bitmap return triggered");
    
  }
  
};

bitmapAdapter.getBitmap(address, BitmapAdapter.TYPE_URL, BitmapAdapter.METHOD_GET, null, returnResponse);
