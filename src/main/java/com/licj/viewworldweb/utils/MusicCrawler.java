package com.licj.viewworldweb.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MusicCrawler {
	private static final Logger logger = Logger.getLogger(MusicCrawler.class);

	public static final String StoreDir = "D:/12-licj/eclipse-workspace/ViewWorldWeb/src/main/java/resource/";
	public static final String BaseUrl = "http://music.163.com";
	public static final String ThemePageUrl = "http://music.163.com/discover/playlist";

	private static List<Theme> themeList = new ArrayList<>();
	private static List<PlayList> pList = new ArrayList<>();
	private static List<PlayDetailList> pDetailList = new ArrayList<>();
	private static final String PlayListOrderHot = "hot";
//	private static final String PlayListOrderNew = "new";

	public MusicCrawler() {
		if (themeList.size() != 0) {
			themeList.clear();
		}
		if (pList.size() != 0) {
			pList.clear();
		}
		if (pDetailList.size() != 0) {
			pDetailList.clear();
		}

	}

	public static void main(String[] args) {
		MusicCrawler musicCrawler = new MusicCrawler();
		// 获取音乐主题列表
		musicCrawler.fetchTheme();
		int[] progress = { 1, 1 };
		themeList.parallelStream().forEach(theme -> {
			// 获取每一种主题下面的播放歌单列表
			musicCrawler.fetchPlayList(theme, PlayListOrderHot, 2, 0);
			System.out.println(progress[0]++ + "|theme|" + theme.getDataCat() + "------>finish!");
		});
		pList.parallelStream().forEach(playList -> {
			// 获取详细主题歌单列表
			musicCrawler.fetchPlayDetailList(playList);
			System.out.println(progress[1]++ + "|playList|" + playList.getListName() + "-->finish!");
		});
		Gson gson = new Gson();
		// 保存最后的结果
		try (OutputStream outStream = new FileOutputStream(StoreDir + "pDetailList.json", false);
				Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));) {
			outWriter.write(gson.toJson(pDetailList));
		} catch (IOException e) {
			logger.error("music crawler keep data error!!!", e);
		}
		System.out.println("keep data complete.");
	}

	/**
	 * 取得所有歌曲的主题以及网址
	 * 
	 * @return MusicCrawler
	 */
	public MusicCrawler fetchTheme() {
		try {
			Document document = Jsoup.connect(ThemePageUrl).header("Referer", "http://music.163.com/")
					.header("Host", "music.163.com").get();
			themeList = document.select("a[class=s-fc1]").stream().map(w -> {
				Theme theme = new Theme();
				theme.setDataCat(w.attr("data-cat"));
				theme.setHref(BaseUrl + w.attr("href"));
				return theme;
			}).collect(Collectors.toList());
		} catch (IOException e) {
			logger.error("music crawler fetch theme error!!!", e);
		}
		return this;
	}

	/**
	 * 取得相应主题条件下的歌单
	 * 
	 * @param theme
	 * @param order
	 * @param limit
	 * @param offset
	 * @return MusicCrawler
	 */
	public MusicCrawler fetchPlayList(Theme theme, String order, int limit, int offset) {
		StringBuilder sb = new StringBuilder();
		sb.append(theme.getHref());
		sb.append("&order=" + order);
		sb.append("&limit=" + limit);
		sb.append("&offset=" + offset * limit);
		String url = sb.toString();
		try {
			Document document = Jsoup.connect(url).header("Referer", "http://music.163.com/")
					.header("Host", "music.163.com").get();
			List<String> list1 = document.select("img[class=j-flag]").stream().map(w -> w.attr("src"))
					.collect(Collectors.toList());
			List<String> list2 = document.select("a[class=msk]").stream().map(w -> w.attr("title"))
					.collect(Collectors.toList());
			List<String> list3 = document.select("a[class=msk]").stream().map(w -> BaseUrl + w.attr("href"))
					.collect(Collectors.toList());
			List<String> list4 = document.select("a[class=nm nm-icn f-thide s-fc3]").stream().map(w -> w.attr("title"))
					.collect(Collectors.toList());
			List<String> list5 = document.select("a[class=nm nm-icn f-thide s-fc3]").stream()
					.map(w -> BaseUrl + w.attr("href")).collect(Collectors.toList());
			List<String> list6 = document.select("span[class=nb]").stream().map(w -> w.text())
					.collect(Collectors.toList());
			for (int i = 0; i < limit; i++) {
				PlayList playList = new PlayList();
				playList.setCoverImgUrl(list1.get(i));
				playList.setListName(list2.get(i));
				playList.setListUrl(list3.get(i));
				playList.setUsrName(list4.get(i));
				playList.setUsrUrl(list5.get(i));
				playList.setPlayTimes(list6.get(i));
				pList.add(playList);
			}
		} catch (IOException e) {
			logger.error("music crawler fetch playlist error!!!", e);
		}
		return this;
	}

	/**
	 * 获取详细的歌单列表内容
	 * 
	 * @param playList
	 * @return MusicCrawler
	 */
	public MusicCrawler fetchPlayDetailList(PlayList playList) {
		try {
			Document document = Jsoup.connect(playList.getListUrl()).header("Referer", "http://music.163.com/")
					.header("Host", "music.163.com").get();
			PlayDetailList playDetailList = new PlayDetailList(playList);
			document.select("script[type=application/ld+json]").stream().findFirst().ifPresent(songInfo -> {
				String songJson = songInfo.data().toString().trim();
				JsonParser parser = new JsonParser(); // 创建JSON解析器
				JsonObject object = (JsonObject) parser.parse(songJson);
				String pubDate = object.get("pubDate").getAsString().split("T")[0];
				playDetailList.setPubDate(pubDate);
			});
			document.select("a[class=u-btni u-btni-fav]").stream().findFirst().ifPresent(w -> {
				playDetailList.setFarvirate(w.attr("data-count"));
			});
			document.select("a[class=u-btni u-btni-share]").stream().findFirst().ifPresent(w -> {
				playDetailList.setShare(w.attr("data-count"));
			});
			document.select("div[id=comment-box]").stream().findFirst().ifPresent(w -> {
				playDetailList.setComment(w.attr("data-count"));
			});
			document.select("span[id=playlist-track-count]").stream().findFirst().ifPresent(w -> {
				playDetailList.setPlaylistTrackCount(w.text());
			});
			document.select("strong[id=play-count]").stream().findFirst().ifPresent(w -> {
				playDetailList.setPlayCount(w.text());
			});
			List<Theme> list1 = document.select("a[class=u-tag]").stream().map(w -> {
				Theme theme = new Theme();
				theme.setDataCat(w.text());
				theme.setHref(BaseUrl + w.attr("href"));
				return theme;
			}).collect(Collectors.toList());
			playDetailList.setThemes(list1);
			List<Song> list2 = document.select("ul[class=f-hide] a").stream().map(w -> {
				Song song = new Song();
				song.setSongName(w.text());
				song.setSongUrl(BaseUrl + w.attr("href"));
				return song;
			}).collect(Collectors.toList());
			playDetailList.setSongs(list2);
			pDetailList.add(playDetailList);
		} catch (IOException e) {
			logger.error("music crawler fetch playDetailList error!!!", e);
		}
		return this;
	}

	/**
	 * 音乐主题类别
	 * 
	 * @author licj
	 */
	class Theme {
		public static final String DATA_CAT = "data_cat";
		public static final String HREF = "href";
		private String dataCat;
		private String href;

		public String getDataCat() {
			return dataCat;
		}

		public void setDataCat(String dataCat) {
			this.dataCat = dataCat;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

	}

	/**
	 * 音乐歌单
	 * 
	 * @author licj
	 */
	class PlayList {
		private String coverImgUrl;
		private String listName;
		private String listUrl;
		private String usrName;
		private String usrUrl;
		private String playTimes;

		public String getCoverImgUrl() {
			return coverImgUrl;
		}

		public void setCoverImgUrl(String coverImgUrl) {
			this.coverImgUrl = coverImgUrl;
		}

		public String getListName() {
			return listName;
		}

		public void setListName(String listName) {
			this.listName = listName;
		}

		public String getListUrl() {
			return listUrl;
		}

		public void setListUrl(String listUrl) {
			this.listUrl = listUrl;
		}

		public String getUsrName() {
			return usrName;
		}

		public void setUsrName(String usrName) {
			this.usrName = usrName;
		}

		public String getUsrUrl() {
			return usrUrl;
		}

		public void setUsrUrl(String usrUrl) {
			this.usrUrl = usrUrl;
		}

		public String getPlayTimes() {
			return playTimes;
		}

		public void setPlayTimes(String playTimes) {
			this.playTimes = playTimes;
		}

	}

	/**
	 * 歌曲名称和地址
	 * 
	 * @author licj
	 */
	class Song {
		public static final String SONG_NAME = "song_name";
		public static final String SONG_URL = "song_url";
		private String songName;
		private String songUrl;

		public String getSongName() {
			return songName;
		}

		public void setSongName(String songName) {
			this.songName = songName;
		}

		public String getSongUrl() {
			return songUrl;
		}

		public void setSongUrl(String songUrl) {
			this.songUrl = songUrl;
		}

	}

	/**
	 * 音乐歌单详细内容
	 * 
	 * @author licj
	 */
	class PlayDetailList extends PlayList {
		public static final String PUB_DATA = "pub_data";
		public static final String FARVIRATE = "farvirate";
		public static final String SHARE = "share";
		public static final String COMMENT = "comment";
		public static final String PLAY_COUNT = "play_count";
		public static final String PLAY_LIST_TRACK_COUNT = "play_list_track_count";
		public static final String THEMES = "themes";
		public static final String SONGS = "songs";
		private String pubDate;
		private String farvirate;
		private String share;
		private String comment;
		private String playCount;
		private String playlistTrackCount;
		private List<Theme> themes = new ArrayList<>();
		private List<Song> songs = new ArrayList<>();
		private PlayList playList;

		public String getPubDate() {
			return pubDate;
		}

		public void setPubDate(String pubDate) {
			this.pubDate = pubDate;
		}

		public String getFarvirate() {
			return farvirate;
		}

		public void setFarvirate(String farvirate) {
			this.farvirate = farvirate;
		}

		public String getShare() {
			return share;
		}

		public void setShare(String share) {
			this.share = share;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public List<Theme> getThemes() {
			return themes;
		}

		public void setThemes(List<Theme> themes) {
			this.themes = themes;
		}

		public List<Song> getSongs() {
			return songs;
		}

		public void setSongs(List<Song> songs) {
			this.songs = songs;
		}

		public String getPlayCount() {
			return playCount;
		}

		public void setPlayCount(String playCount) {
			this.playCount = playCount;
		}

		public String getPlaylistTrackCount() {
			return playlistTrackCount;
		}

		public void setPlaylistTrackCount(String playlistTrackCount) {
			this.playlistTrackCount = playlistTrackCount;
		}

		public PlayList getPlayList() {
			return playList;
		}

		public void setPlayList(PlayList playList) {
			this.playList = playList;
		}

		public PlayDetailList(PlayList playList) {
			this.playList = playList;
		}

		@Override
		public String getCoverImgUrl() {
			return this.playList.getCoverImgUrl();
		}

		@Override
		public void setCoverImgUrl(String coverImgUrl) {
			this.playList.setCoverImgUrl(coverImgUrl);
		}

		@Override
		public String getListName() {
			return this.playList.getListName();
		}

		@Override
		public void setListName(String listName) {
			this.playList.setListName(listName);
		}

		@Override
		public String getListUrl() {
			return this.playList.getListUrl();
		}

		@Override
		public void setListUrl(String listUrl) {
			this.playList.setListUrl(listUrl);
		}

		@Override
		public String getUsrName() {
			return this.playList.getUsrName();
		}

		@Override
		public void setUsrName(String usrName) {
			this.playList.setUsrName(usrName);
		}

		@Override
		public String getUsrUrl() {
			return this.playList.getUsrUrl();
		}

		@Override
		public void setUsrUrl(String usrUrl) {
			this.playList.setUsrUrl(usrUrl);
		}

		@Override
		public String getPlayTimes() {
			return this.playList.getPlayTimes();
		}

		@Override
		public void setPlayTimes(String playTimes) {
			this.playList.setPlayTimes(playTimes);
		}

	}
}
