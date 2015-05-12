# BitmapAdapter
An Android class for handling bitmap loading from file or network/Internet<br />
<br />
This can be called using a single method<br />
getBitmap(String path, Integer type, Integer method, ImageView imageView, BitmapResponse response)<br />
path: path to the file, filesystem path or url string<br />
type: url or file request, TYPE_URL or TYPE_FILE<br />
method: return the bitmap or set the bitmap to imageview, METHOD_GET or METHOD_SET<br />
imageView: the imageview to set the bitmap to or null<br />
response: the BitmapResponse callback or null<br />
<br />
Set up a BitmapResponse callback like so<br />
BitmapAdapter.BitmapResponse returnResponse = new BitmapAdapter.BitmapResponse() {<br />
  @Override<br />
  public void bitmapReturn(Bitmap bitmap) {<br />
    Log.i(TAG, "Bitmap return triggered");<br />
  }<br />
};<br />
<br />
bitmapAdapter.getBitmap(address, BitmapAdapter.TYPE_URL, BitmapAdapter.METHOD_GET, null, returnResponse);
