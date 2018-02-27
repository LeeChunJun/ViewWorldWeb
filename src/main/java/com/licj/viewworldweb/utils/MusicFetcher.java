package com.licj.viewworldweb.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 按参数获取网易云音乐的歌单列表
 * 
 * @author licj
 *
 */
public class MusicFetcher {
	public static final String output = "E:/12/ViewWorldWeb/src/main/java/resource/";
	public static final String DiscoverUrl = "http://music.163.com/discover/playlist/";
	public static final String MusicListUrl = "http://music.163.com";
	public static final String SongUrl = "http://music.163.com/song?id=";
	public static final String LyricUrl = "http://music.163.com/api/song/media?id=";

	public static void main(String[] args) {
		try {
			
			fetchMusicList();
//			fetchMusicLike();
//			fetchMusicRate();
//			fetchMusicItem();
//			fetchMusicUser();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// url="?order=hot&cat=全部&limit=35&offset=0"
	public static void fetchMusicList() throws IOException {
		int totalPage = 37;
		String url = "?order=hot&cat=全部&limit=35&offset=";

		OutputStream out = new FileOutputStream(output + "music_list.csv", false);
		try (Writer outWriter = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8))) {
			outWriter.write("ListName,");
			outWriter.write("ListUrl,");
			outWriter.write("UserName,");
			outWriter.write("UserUrl,");
			outWriter.write("CoverUrl,");
			outWriter.write("PlayTime");
			outWriter.write("\n");
		}

		for (int i = 0; i < totalPage; i++) {
			Document document = Jsoup.connect(DiscoverUrl + url + i * 35).header("Referer", "http://music.163.com/")
					.header("Host", "music.163.com").get();

			List<String> coverUrls = document.select("ul[id=m-pl-container] img").stream().map(w -> w.attr("src"))
					.collect(Collectors.toList());

			List<String> playTimes = document.select("ul[id=m-pl-container] span[class=nb]").stream().map(w -> w.text())
					.collect(Collectors.toList());

			List<String> musicListNames = new ArrayList<>();
			List<String> musicListUrls = new ArrayList<>();
			document.select("ul[id=m-pl-container] p[class=dec] a").stream().map(w -> {
				musicListNames.add(w.text());
				musicListUrls.add(w.attr("href"));
				return w.text() + "-->" + w.attr("href");
			}).forEach(System.out::println);

			List<String> musicUserNames = new ArrayList<>();
			List<String> musicUserUrls = new ArrayList<>();
			document.select("ul[id=m-pl-container] a[class=nm nm-icn f-thide s-fc3]").stream().map(w -> {
				musicUserNames.add(w.text());
				musicUserUrls.add(w.attr("href"));
				return w.text() + "-->" + w.attr("href");
			}).forEach(System.out::println);

			System.out.println("coverUrls:" + coverUrls.size());
			System.out.println("playTimes:" + playTimes.size());
			System.out.println("musicListNames:" + musicListNames.size());
			System.out.println("musicListUrls:" + musicListUrls.size());
			System.out.println("musicUserNames:" + musicUserNames.size());
			System.out.println("musicUserUrls:" + musicUserUrls.size());

			try (
					OutputStream outStream = new FileOutputStream(output + "music_list.csv", true);
					Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));
				) {

				for (int j = 0; j < musicListNames.size(); j++) {
					outWriter.write(musicListNames.get(j).replace(",","，"));
					outWriter.write(',');
					outWriter.write(musicListUrls.get(j));
					outWriter.write(',');
					outWriter.write(musicUserNames.get(j).replace(",","，"));
					outWriter.write(',');
					outWriter.write(musicUserUrls.get(j));
					outWriter.write(',');
					outWriter.write(coverUrls.get(j));
					outWriter.write(',');
					outWriter.write(playTimes.get(j));
					outWriter.write('\n');
				}

			}
		}

	}

	// url="/playlist?id=2032847837"
	public static void fetchMusicLike() throws IOException {
		OutputStream out = new FileOutputStream(output + "music_like.csv", false);
		try (Writer outWriter = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8))) {
			outWriter.write("UserName,");
			outWriter.write("UserUrl,");
			outWriter.write("UserCreateTime,");
			outWriter.write("SongName,");
			outWriter.write("SongUrl,");
			outWriter.write("LikeUserName,");
			outWriter.write("LikeUserUrl");
			outWriter.write("\n");
		}
		try (Stream<String> stream = Files.lines(Paths.get(output + "music_list.csv"))) {

			stream.skip(1).map(line -> line.split(",")[1]).map(url -> {
				Document doc = null;
				try {
					doc = Jsoup.connect(MusicListUrl + url).header("Referer", "http://music.163.com/")
							.header("Host", "music.163.com").get();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return doc;
			}).filter(doc -> doc != null).forEach(doc -> {
				final String[] userName = {""};
				final String[] userUrl = {""};
				final String[] userCreateTime = {""};
				List<String> songNames = new ArrayList<>();
				List<String> songUrls = new ArrayList<>();
				List<String> likeUserNames = new ArrayList<>();
				List<String> likeUserUrls = new ArrayList<>();
				doc.select("span[class=name] a").stream().forEach(w -> {
					userName[0] = w.text();
					userUrl[0] = w.attr("href");
				});
				doc.select("span[class=time s-fc4]").stream().forEach(w -> {
					userCreateTime[0] = w.text();
				});
				doc.select("ul[class=f-hide] a").stream().forEach(w -> {
					songNames.add(w.text());
					songUrls.add(w.attr("href"));
				});
				doc.select("ul[class=m-piclist f-cb] a").stream().forEach(w -> {
					likeUserNames.add(w.attr("title"));
					likeUserUrls.add(w.attr("href"));
				});
				try (
						OutputStream outStream = new FileOutputStream(output + "music_like.csv", true);
						Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));
					) {
						for (int i = 0; i < songNames.size(); i++) {
							outWriter.write(userName[0].replace(",","，"));
							outWriter.write(',');
							outWriter.write(userUrl[0]);
							outWriter.write(',');
							outWriter.write(userCreateTime[0].substring(0, userCreateTime[0].length() - 3));
							outWriter.write(',');
							outWriter.write(songNames.get(i).replace(",","，"));
							outWriter.write(',');
							outWriter.write(songUrls.get(i));
							outWriter.write(',');
							outWriter.write(likeUserNames.stream().collect(Collectors.joining(":")).replace(",","，"));
							outWriter.write(',');
							outWriter.write(likeUserUrls.stream().collect(Collectors.joining(":")));
							outWriter.write('\n');
						}
						System.out.println("songNames:" + songNames.size());
					
				} catch (Exception e) {

				}
			});

		}
		
	}

	public static void fetchMusicRate() throws IOException{
		File file = new File(output + "music_rate.csv");
		if(file.exists()){
			file.delete();
		}
		try (Stream<String> stream = Files.lines(Paths.get(output + "music_like.csv"))) {
			stream.skip(1).forEach(line -> {
				final String[] userID = {""};
				final String[] itemID = {""};
				final String score = "5.0";
				final String[] timestamp = {""};
				
				String userIDStr = line.split(",")[1];
				userID[0] = userIDStr.substring(userIDStr.lastIndexOf("id=") + 3, userIDStr.length());
				
				String itemIDStr = line.split(",")[4];
				itemID[0] = itemIDStr.substring(itemIDStr.lastIndexOf("id=") + 3, itemIDStr.length());
				
				String timestampStr = line.split(",")[2];
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					timestamp[0] = String.valueOf(sdf.parse(timestampStr).getTime() / 1000);
				} catch (ParseException e) {
					
				}
			
				try (
						OutputStream outStream = new FileOutputStream(output + "music_rate.csv", true);
						Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));
					) {
						outWriter.write(userID[0]);
						outWriter.write(',');
						outWriter.write(itemID[0]);
						outWriter.write(',');
						outWriter.write(score);
						outWriter.write(',');
						outWriter.write(timestamp[0]);
						outWriter.write('\n');
						System.out.println(itemID[0]);
					
				} catch (Exception e) {

				}
			});
		}

	}
	
	public static void fetchMusicItem() throws IOException{
		OutputStream out = new FileOutputStream(output + "music_item.csv", false);
		try (Writer outWriter = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8))) {
			outWriter.write("ID,");
			outWriter.write("SONG_NAME,");
			outWriter.write("SINGER_NAME,");
			outWriter.write("PIC_URL,");
			outWriter.write("PUBLISH_TIME,");
			outWriter.write("LYRIC,");
			outWriter.write("TAGS");
			outWriter.write("\n");
		}
		try (Stream<String> stream = Files.lines(Paths.get(output + "music_rate.csv"))) {
			
			stream.map(line -> {
				return line.split(",")[1];
			}).distinct().parallel().map(songID -> {
				Document doc = null;
				try {
					doc = Jsoup.connect(SongUrl + songID)
							.header("Referer", "http://music.163.com/")
							.header("Host", "music.163.com").get();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return doc;
			}).filter(doc -> doc != null)
			.forEach(doc -> {
				final String[] id = {""};
				final String[] song_name = {""};
				final String[] singer_name = {""};
				final String[] pic_url = {""};
				final String[] publish_time = {""};
				final String[] lyric = {""};
				final String[] tags = {""};
				
				doc.select("script[type=application/ld+json]").stream()
						.findFirst()
						.ifPresent(songInfo -> {
							String songJson = songInfo.data().toString().trim();
							
							JsonParser parser = new JsonParser();  //创建JSON解析器
							JsonObject object = (JsonObject) parser.parse(songJson);
							String idTemp = object.get("@id").getAsString();
							id[0] = idTemp.substring(idTemp.lastIndexOf("id=") + 3, idTemp.length());
							song_name[0] = object.get("title").getAsString().replace(",", "，");
							pic_url[0] = object.get("images").getAsString();
							String[] singerAndlist = object.get("description").getAsString().replace(",", "，").split("：|。");
							singer_name[0] = singerAndlist[1];
							if(singerAndlist.length == 4){
								tags[0] = singerAndlist[1] + ":" + singerAndlist[3];
							} else {
								tags[0] = singerAndlist[1];
							}
							publish_time[0] = object.get("pubDate").getAsString().split("T")[0];
							
							System.out.println(id[0]);
						});
				
				String lyricInfo = null;
				try {
					lyricInfo = Jsoup.connect(LyricUrl + id[0])
							.ignoreContentType(true)
							.get().text();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(lyricInfo != null){
					JsonParser lyricParser = new JsonParser();  //创建JSON解析器
					try {
						JsonObject lyricObject = (JsonObject) lyricParser.parse(lyricInfo);
						String tempLyric = lyricObject.get("lyric").getAsString();
						
						lyric[0] = Base64.getEncoder().encodeToString(tempLyric.getBytes("UTF-8"));
//						lyric[0] = new String(Base64.getDecoder().decode(lyric[0]), "UTF-8")
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (Exception e) {
						System.err.println("以上歌曲没有歌词！");
					}
				}
				
				try (
					OutputStream outStream = new FileOutputStream(output + "music_item.csv", true);
					Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));
				) {
						outWriter.write(id[0]);
						outWriter.write(',');
						outWriter.write(song_name[0]);
						outWriter.write(',');
						outWriter.write(singer_name[0]);
						outWriter.write(',');
						outWriter.write(pic_url[0]);
						outWriter.write(',');
						outWriter.write(publish_time[0]);
						outWriter.write(',');
						outWriter.write(lyric[0]);
						outWriter.write(',');
						outWriter.write(tags[0]);
						outWriter.write('\n');
				} catch (Exception e) {

				}
			});
			
			
		}

	}

	public static void fetchMusicUser() throws IOException{
		OutputStream out = new FileOutputStream(output + "music_user.csv", false);
		try (Writer outWriter = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8))) {
			outWriter.write("ID,");
			outWriter.write("NAME,");
			outWriter.write("EMAIL,");
			outWriter.write("PASSWORD,");
			outWriter.write("PHONE,");
			outWriter.write("TAGS,");
			outWriter.write("\n");
		}
		try (Stream<String> stream = Files.lines(Paths.get(output + "music_rate.csv"))) {
			int[] index = {1};
			stream.map(line -> {
				return line.split(",")[0];
			}).distinct().forEach(userID -> {
				System.out.println(userID);
				try (
						OutputStream outStream = new FileOutputStream(output + "music_user.csv", true);
						Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));
					) {
					
						outWriter.write(userID);
						outWriter.write(',');
						outWriter.write("user" + userID);
						outWriter.write(',');
						outWriter.write("user" + userID + "@gmail.com");
						outWriter.write(',');
						outWriter.write("password" + userID);
						outWriter.write(',');
						outWriter.write(getUserPhone(index[0]++));
						outWriter.write(',');
						outWriter.write("tags" + userID);
						outWriter.write('\n');
					
				} catch (IOException e) {

				}
			});
			
		}
			
	}
	
	public static String getUserPhone(int index){
		String phone = "";
		if (index < 10) {
			phone = "1827083700" + index;
		} else if(index >= 10 && index < 100){
			phone = "182708370" + index;
		} else if(index >= 100 && index < 1000){
			phone = "18270837" + index;
		} else if(index >= 1000 && index < 10000){
			phone = "1827083" + index;
		}
		return phone;
	}

}
