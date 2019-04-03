package asu.gunma;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.SimpleDirectionGestureDetector;
import asu.gunma.ui.screen.menu.TitleScreen;


public class GunmaChan extends Game {
	// Temporary values
	public static int WIDTH = 1024;
	public static int HEIGHT = 600;
	public static final String TITLE = "Gunma-chan Game";
	public Music background_music;
	public ActionResolver speechGDX;
	public DbInterface dbCallback;
	private static float masterVolume = 5;
	private ArrayList<VocabWord> activeVocabList;
	public Preferences prefs;

		public GunmaChan() {}
		public GunmaChan(ActionResolver speechGDX, DbInterface dbCallback){
			this.speechGDX = speechGDX;
			this.dbCallback = dbCallback;
		}

		@Override
		public void create() {
			Preferences prefs = Gdx.app.getPreferences("MyPreferences");

			//test
			if(!prefs.contains("active1")) {
				System.out.println("Contains active1");
				prefs.putBoolean("active1", true);
				for(VocabWord v : dbCallback.getDbVocab()){
					if(v.getModuleCategory().equals("Alphabet")){
						activeVocabList.add(v);
					}
				}
			}
			if(!prefs.contains("active2")) {
				prefs.putBoolean("active2", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Colors-Shapes")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active3")) {
				prefs.putBoolean("active3", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Countries")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active4")) {
				prefs.putBoolean("active4", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Days-Months")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active5")) {
				prefs.putBoolean("active5", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Feelings")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active6")) {
				prefs.putBoolean("active6", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Fruits-Food")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active7")) {
				prefs.putBoolean("active7", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Numbers")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active8")) {
				prefs.putBoolean("active8", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Places")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active9")){
				prefs.putBoolean("active9", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Professions")) {
						activeVocabList.add(v);
					}
				}
			}


			if(!prefs.contains("active10")) {
				prefs.putBoolean("active10", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Subjects")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active11")) {
				prefs.putBoolean("active11", true);
				for (VocabWord v : dbCallback.getDbVocab()) {
					if (v.getModuleCategory().equals("Time")) {
						activeVocabList.add(v);
					}
				}
			}

			if(!prefs.contains("active12"))
				prefs.putBoolean("active12", false);

			if(!prefs.contains("active13"))
				prefs.putBoolean("active13", false);

			if(!prefs.contains("active14"))
				prefs.putBoolean("active14", false);

			if(!prefs.contains("active15"))
				prefs.putBoolean("active15", false);

			background_music = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
			background_music.setLooping(false);
			background_music.setVolume(masterVolume);
			background_music.play();
			activeVocabList = new ArrayList<>();
			List<VocabWord> dbVocab = dbCallback.getDbVocab();
			activeVocabList.addAll(dbVocab);

			//System.out.println("ACTIVE WORD LIST");
			for(VocabWord v : activeVocabList){
				//System.out.println(v.getEngSpelling());
			}
			this.setScreen(new TitleScreen(this, speechGDX, dbCallback, background_music, activeVocabList, prefs));
		}

		@Override
		public void render () {
			super.render();
		}

		@Override
		public void dispose () {
			// close database here
			background_music.dispose();
			super.dispose();
		}

		@Override
		public void resize(int width, int height) {
			super.resize(width, height);
		}

		@Override
		public void pause() {
			super.pause();

		}

		@Override
		public void resume() {
			super.resume();
		}
	}

