package pw.oxcafebabe.mstojcevich.gridrace.map;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcusant on 9/21/2014.
 */
public class Map {

    private final boolean[][] blockValidity;

    public Map(InputStream mapFileStream) throws IOException {
        BufferedReader mapReader = new BufferedReader(new InputStreamReader(mapFileStream));
        List<Boolean[]> validityList = new ArrayList<Boolean[]>();
        String currentLine;
        while((currentLine = mapReader.readLine()) != null) {
            List currentLineValidity = new ArrayList<Boolean>();
            for(char c : currentLine.toCharArray()) {
                currentLineValidity.add(c == 1 ? true : false);
            }
            validityList.add((Boolean[])currentLineValidity.toArray());
        }
        blockValidity = validityList.toArray(new boolean[][]{});
    }

    public boolean isBlockValid(int x, int y) {
        return blockValidity[y][x];
    }

}
