package me.undownding.music;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.baidu.music.model.Album;
import com.baidu.music.model.AlbumList;
import com.baidu.music.model.BaseObject;
import com.baidu.music.model.OAuthException;
import com.baidu.music.model.SearchResult;
import com.baidu.music.onlinedata.OnlineManagerEngine;
import com.baidu.music.util.LogUtil;

import java.util.List;

import me.undownding.music.model.OAuthHelper;
import me.undownding.music.model.AlbumModel;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testSearch() throws OAuthException {
        createApplication();
        OAuthHelper.Companion.auth(getApplication());
        SearchResult result = OnlineManagerEngine.getInstance(getApplication()).getSearchManager(getApplication())
                .searchMusic("Wonderzone", 0, 80);
        assertEquals(result.getErrorCode(), BaseObject.OK);
    }

    public void testArtist() {
        createApplication();
        OAuthHelper.Companion.auth(getApplication());
        final AlbumList result = OnlineManagerEngine.getInstance(getApplication()).getArtistManager(getApplication())
                .getArtistAlbumListSync(getApplication(), 56468493, 0, 80);
        assertEquals(result.getErrorCode(), BaseObject.OK);
    }

    public void testAlbum() throws OAuthException {
        createApplication();
        OAuthHelper.Companion.auth(getApplication());
        Album list = OnlineManagerEngine.getInstance(getApplication()).getAlbumManager(getApplication())
                .getLosslessAlbum(38671304);
        assertEquals(list.getErrorCode(), BaseObject.OK);
    }

    public void testRxGetAlbums() throws InterruptedException {
        createApplication();
        OAuthHelper.Companion.auth(getApplication());
        LogUtil.setDebugMode(true);
        com.baidu.utils.LogUtil.setDebugMode(true);
        final List<Album> list = AlbumModel.Companion.requestAlbums().toBlocking().single();
        assertFalse(list.size() == 0);
    }
}