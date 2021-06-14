/*

// These extensions are used in WinBounds,
see examples in:
WinBounds.help;

// find and show window;
// also get and set their bounds - now better with WinBounds


// make 3 windows
~names = [\abc, \bcd, \cde];
~names.do {|name| Window(name).front };

// find them by name or partial name
Window.find(\a).front
Window.findAll(\b)
Window.findAll(\c)

Window.find(\a).moveTo(200).front;
Window.find(\bcd).moveTo(200, 200).front;
Window.findAll(\c).do(_.front);

// restore
~locs = Window.getAll.postcs;
Window.findAll(\c).do(_.moveTo(800.rand, 800.rand));
Window.setAll(~locs, true);

Window.getAllCS;

*/

+ Point {
	flipY { ^x @ (Window.screenBounds.height - y) }
}

+ Rect {
	flipY {
		var flippedTop = Window.screenBounds.height - this.top - this.height;
		^Rect( left, flippedTop, width, height );
	}
}

+ Window {

	*find { |name|
		name = name.asString;
		^Window.allWindows.detect { |w| w.name.contains(name) };
	}
	*findAll { |name|
		name = name.asString;
		^Window.allWindows.select { |w| w.name.contains(name) };
	}

	*show { |name|
		var found = Window.find(name);
		found !? { defer { found.front } };
		^found
	}

	*getAll {
		^Window.allWindows.collect { |win| [win.name, win.bounds] };
	}

	*getAllCS {
		var prettyCS = Window.getAll.cs
		.replace("[ [", "[\n\t[")
		.replace("], [", "],\n\t[");
		^("Window.setAll(" ++ prettyCS ++ ");")
	}

	*setAll { |pairs, toFront = false|
		pairs.do { |pair|

			var win = Window.allWindows.detect { |w| w.name == pair[0] };

			if (win.notNil) {
				win.bounds = pair[1];
				if (toFront) { win.front };
			};
		};
	}
	moveTo { |left, top|
		var bounds = this.bounds;
		this.bounds_(bounds.left_(left ? bounds.left).top_(top ? bounds.top))
	}

	width_ { |width|
		this.bounds(this.bounds.width_(width))
	}

	height_ { |height|
		this.bounds(this.bounds.flipY.height_(height).flipY)
	}
}
