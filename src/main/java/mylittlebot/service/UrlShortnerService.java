package mylittlebot.service;

import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.shorten;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class UrlShortnerService {

	public String shortUrl(String url) {
		return as("o_v2j1l23am", "R_194d52945187433d8ba71b96a09b9480").call
				(shorten(url)).getShortUrl();
	}
}
