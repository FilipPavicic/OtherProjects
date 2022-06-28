package hr.fer.ooup.lab4.models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.ooup.lab4.Point;
import hr.fer.ooup.lab4.Renderer;
import hr.fer.ooup.lab4.models.Oval;

public class SVGRendererImpl implements Renderer {

	private List<String> lines = new ArrayList<>();
	private Path filePath;
	
	public SVGRendererImpl(Path filePath) {
		this.filePath = filePath;
		lines.add(
			"""	
				<svg xmlns="http://www.w3.org/2000/svg"
			    xmlns:xlink="http://www.w3.org/1999/xlink">
			"""
		);
	}

	public void close() throws IOException {
		lines.add("</svg>");
		if(filePath != null) {
			byte[] podatci = lines.stream().collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8);
			Files.write(filePath, podatci);
		}
		// u lines još dodaj završni tag SVG dokumenta: </svg>
		// sve retke u listi lines zapiši na disk u datoteku
		// ...
	}
	
	@Override
	public void drawLine(Point s, Point e) {
		lines.add("<line x1=\""+s.getX()+"\"  y1=\""+s.getY()+"\" x2=\""+e.getX()+"\"   y2=\""+e.getY()+"\" style=\"stroke:#0000FF;\"/>");
	}

	@Override
	public void fillPolygon(Point[] points) {
		String start = "<polygon points=\"";
		String end = "\" style=\"stroke:#FF0000; fill:#0000FF;\"/>";
		lines.add(Stream.of(points).
			map(p -> p.getX() + "," + p.getY()).
			collect(Collectors.joining(" ",start,end))
			);
	}

}