package nafos.core.util;




public class UriUtil {

	/**
	 * 截取uri
	 * @param uri
	 * @return
	 */
	public static String parseUri(String uri) {
		// 1)null
		if (null == uri) {
			return null;
		}
		// 2)截断?
		int index = uri.indexOf("?");
		if (-1 != index) {
			uri = uri.substring(0, index);
		}
		return uri;
	}


}
