package geral;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class Util {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter
		.ofLocalizedDate(FormatStyle.MEDIUM);
	
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter
		.ofLocalizedTime(FormatStyle.MEDIUM);
	
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
		.ofLocalizedDateTime(FormatStyle.MEDIUM);
	
	private static final Encoder base64 = Base64.getUrlEncoder();
	
	public static final String getUUID() {
		UUID uuid = UUID.randomUUID();
		byte[] bytes = ByteBuffer.wrap(new byte[16])
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits())
            .array();
		return base64.encodeToString(bytes).substring(0, 22);
	}
	
	public static String getInputStreamAsString(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.lines().collect(Collectors.joining(System.lineSeparator()));
	}
	
	public static String getResourceAsString(String name) throws FileNotFoundException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream(name);
	    if (is != null)
	    	return getInputStreamAsString(is);
	    else
	    	throw new FileNotFoundException(name);
	}
	
	public static ImageIcon getResourceAsImageIcon(String name) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("imagem/" + name);
		return url == null ? null : new ImageIcon(url);
	}
	
	public static Image getResourceAsImage(String name) {
		ImageIcon imageIcon = getResourceAsImageIcon(name);
		return imageIcon == null ? null : imageIcon.getImage();
	}
	
	public static String getAppAbsolutePath(){
		try {
			return new File(Util.class
				.getProtectionDomain()
				.getCodeSource()
				.getLocation()
				.toURI()
				.getPath())
					.getParentFile()
					.getAbsolutePath() + File.separator;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean equals(Object a, Object b) {
		return (a == null && b == null) || (a != null && a.equals(b));
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}
	
	public static String ifNull(String s, String nullValue) {
		return s == null ? nullValue : s;
	}
	
	public static String emptyIfNull(String s) {
		return ifNull(s, "");
	}
		
	public static String toString(LocalDate d) {
		return d.format(dateFormatter);
	}
	
	public static String toString(LocalTime t) {
		return t.format(timeFormatter);
	}
	
	public static String toString(LocalDateTime dt) {
		return dt.format(dateTimeFormatter);
	}
		
	public static String getDigits(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		for (char c : s.toCharArray()) {
			if (c >= '0' && c <= '9')
				sb.append(c);
		}
		return sb.toString();
	}
	
	public static String strZero(Object o, int len) {
		String s = o.toString();
		while (s.length() < len)
			s = '0' + s;
		return s;
	}
		
	public static LocalDate toLocalDate(String s) throws Exception {
		if (isNullOrEmpty(s))
			return null;
		else {
			LocalDate now = LocalDate.now();
			String month = strZero(now.getMonthValue(), 2);
			String year = strZero(now.getYear(), 4);
			String era = strZero(now.getYear() / 100, 2);
			String digits = getDigits(s);
			char sep = '/';
			int len = digits.length();
			switch (len) {
				case 1: s = "0" + digits + sep + month + sep + year; break;
				case 2: s = digits + sep + month + sep + year; break;
				case 4: s = digits.substring(0,2) + sep + digits.substring(2,4) + sep + year; break;
				case 6: s = digits.substring(0,2) + sep + digits.substring(2,4) + sep + era + digits.substring(4,6); break;
				case 8: s = digits.substring(0,2) + sep + digits.substring(2,4) + sep + digits.substring(4,8); break;
			}
			return LocalDate.parse(s, dateFormatter);
		}
	}
		
	public static LocalTime toLocalTime(String s) {
		if (isNullOrEmpty(s))
			return null;
		else {
			String digits = getDigits(s);
			int len = digits.length();
			switch (len) {
				case 1: s = "0" + digits + ":00"; break;
				case 2: s = digits + ":00"; break;
				case 4: s = digits.substring(0,2) + ":" + digits.substring(2,4); break;
				case 6: s = digits.substring(0,2) + ":" + digits.substring(2,4) + ":" + digits.substring(4,6); break;
			}
			return LocalTime.parse(s);
		}
	}
	
	/*///
	public static String fmtNumber(Double value, int decimals) {
		return String.format("%,." + decimals + "f", value);
	}
	
	public static String fmtMoney(Double value) {
		return fmtNumber(value, 2);
	}
	*/
	
	public static Double toDouble(String s) throws Exception {
		char c;
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		char decimalSeparator = dfs.getDecimalSeparator();
		char minusSign = dfs.getMinusSign();
		String temp = "";
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (c == decimalSeparator)
				temp += '.';
			else if (c == minusSign || (c >= '0' && c <= '9'))
				temp += c;
		}
		return Double.parseDouble(temp);
	}
	
	public static void add(JComboBox<Object> cbx, Object[] items) {
		for (Object o : items)
			cbx.addItem(o);
	}
		
	public static void zip(File inputFile, File outputFile) 
			throws FileNotFoundException, IOException {
		final int BUFFER_SIZE = 4096;
		int bytesRead;
		byte[] bytes = new byte[BUFFER_SIZE];
		try(FileInputStream fis = new FileInputStream(inputFile);
			BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
			FileOutputStream fos = new FileOutputStream(outputFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ZipOutputStream zos = new ZipOutputStream(bos)) {
				ZipEntry entry = new ZipEntry(inputFile.getName());
				zos.putNextEntry(entry);
				while((bytesRead = bis.read(bytes, 0, BUFFER_SIZE)) != -1)
					zos.write(bytes, 0, bytesRead);
				zos.close();
		}
	}
	
	public static String getTempDir() {
		 String s = System.getProperty ("java.io.tmpdir");
		 if (!s.endsWith(File.separator))
			s += File.separator;
		 return s;
	}
	
	public static String md5(String s) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(s.getBytes());
		return new BigInteger(1, md.digest()).toString(16);		
	}
		
}
