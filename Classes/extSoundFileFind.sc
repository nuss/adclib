

///// find 4 default sounds in SC resourceDir:
// SoundFile.pathMatch(Platform.resourceDir +/+ "sounds/a11*.aiff");
// SoundFile.pathMatch("~/*/*.*").size;
// SoundFile.pathMatch("~/*/*/*.*").size;


////// three intended fails:
// SoundFile.exists("nope/no/file/there.wav");
// SoundFile.exists("this/is/a/folder/");
// SoundFile.exists("this/is/a/forbidden.psd");
////// psd open bug says something
// SoundFile.openRead("/Users/adc/Pictures/DragonFyrAll.psd");
// SoundFile.exists("/Users/adc/Pictures/DragonFyrAll.psd");

////// true:
// SoundFile.exists(Platform.resourceDir +/+ "sounds/a11wlk01-44_1.aiff");
// should not crash (libsndfile bug with old apple resource fork)
// SoundFile.openRead("/Users/adc/Pictures/DragonFyrAll.psd")
// SoundFile.pathMatch("/Users/adc/Pictures/DragonFyrAll.psd")
// SoundFile.pathMatch(Platform.resourceDir +/+ "sounds/a11*.aiff");
// SoundFile.find([Platform.resourceDir +/+ "sounds/a11*.aiff"]).flat;
// SoundFile.find(Platform.resourceDir +/+ "sounds/""*").flat;
//
// SoundFile.find(Platform.resourceDir +/+ "sounds/""*", { |sf| sf.duration > 1 });
// SoundFile.find(Platform.resourceDir +/+ "sounds/""*", { |sf| sf.duration <= 1 });
//
// SoundFile.find("~/*/*.*").do { |sf| sf.path.basename.postcs };
////// works with array of paths:
// SoundFile.find(["~/*/*.wav", "~/*/*.aif"]).do(_.do { |sf| sf.path.basename.postcs });




+ SoundFile {

	// find all readable soundfiles at path patterns
	// that pass the test function
	*find { |paths, test = true|
		if (paths.isKindOf(String)) { paths = paths.bubble };
		^paths.collect { |pathpat|
			pathpat.pathMatch.collect { |path|
				// info closes soundfile if opened
				SoundFile(path).info;
			}.select { |sndfile|
				sndfile.notNil and: {
					test.value(sndfile)
				}
			}
		}.unbubble
	}

	*pathMatch { |path|
		^path.pathMatch.select(this.exists(_))
	}

	*exists { |path, postExcluded = false|
		var sf, exists;
		sf = this.new(path);
		exists = sf.openRead(path, postExcluded);
		if (sf.isOpen) { sf.close };
		^exists
	}
}
