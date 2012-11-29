The Seystertron
===============

A good friend of mine is an absolutely wicked synth player.  He
introduced my to digital synthesizers when we were in a band together
in high school.  I'm not going to say anything here about whether we
were any good except to comment that we had a _wicked_ synth player.

Seeing him tweak his synth for maximum awesomeness got me interested
in how digital synthesizers work, so in college I spent a week or so
one summer putting this together: a rudimentary digital synthesizer.
My friend suggested I name it The Seystertron, after myself, and I'm
pretty full of myself, so it didn't take much convincing.

Sadly, Java had (and still has) pretty bad audio latency, so playing
the synth like a piano wouldn't really be fun.  Instead, you can
configure a one measure repeating pattern, and while the pattern is
playing, you can tweak the synthesizer parameters for whatever level
of awesomeness suits you best.

The Seystertron starts up with a wicked loop already loaded into the
pattern editor, thanks to aforementioned wicked synth player Robert
Kipp, who contributed it.

Build
====

The script build.sh builds the game sources and archives them in a JAR
file.  There is also an example HTML file that shows how to embed the
JAR file as an applet.
