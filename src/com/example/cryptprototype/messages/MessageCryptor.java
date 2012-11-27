package com.example.cryptprototype.messages;

public class MessageCryptor {
	public static final int FALSE = 0;
	public static final int TRUE = 1;

	public static final int TYPE1 = -1;
	public static final int NONE = 0;
	public static final int RIJNDAEL = 1;

	private static final int KEYSIZE = 128;
	private static final byte[] cipherKey = { 73, 32, 108, 111, 118, 101, 32, 117, 32, 77, 97, 114, 116, 104, 97, 33 };

	public static CharSequence encrypt(CharSequence s, int type) {
		switch(type) {
			case TYPE1:
				return typeOneEncrypt(s);
			case RIJNDAEL:
				return AESEncrypt(s);
			default:
				return s;
		}
	}

	public static CharSequence decrypt(CharSequence s, int type) {
		switch(type) {
			case TYPE1:
				return typeOneDecrypt(s);
			case RIJNDAEL:
				return AESDecrypt(s);
			default:
				return s;
		}
	}

	private static CharSequence AESEncrypt(CharSequence s) {
		Rijndael encryptor = new Rijndael();
		encryptor.makeKey(cipherKey, KEYSIZE, Rijndael.DIR_ENCRYPT);
		String[] textblocks = blockify(s.toString());
		String result = "";
		for (int i = 0; i < textblocks.length; i++) {
			byte[] pt = stringToBytes(textblocks[i]); // plaintext block
			byte[] ct = new byte[pt.length]; // ciphertext block
			encryptor.encrypt(pt, ct);
			for (int j = 0; j < ct.length; j++)
				result = result.concat(byteToBinary(ct[j]));
		}
		return result;
	}

	private static CharSequence AESDecrypt(CharSequence s) {
		Rijndael decryptor = new Rijndael();
		decryptor.makeKey(cipherKey, KEYSIZE, Rijndael.DIR_DECRYPT);
		String[] textblocks = blockifyBinary(s.toString());
		String result = "";
		for (int i = 0; i < textblocks.length; i++) {
			byte[] ct = binaryToBytes(textblocks[i]); // ciphertext block
			byte[] pt = new byte[ct.length]; // plaintext block
			decryptor.decrypt(ct, pt);
			result = result.concat(new String(pt));
		}
		result = removeEndingSpace(result);
		return result;
	}

	private static CharSequence typeOneEncrypt(CharSequence s) {
		String result = "";
		for (int i = 0; i < s.length(); i++) {
			result = result.concat(typeOneCharEncrypt(s.charAt(i)));
		}
		return result;
	}

	private static CharSequence typeOneDecrypt(CharSequence s) {
		String result = "";
		for (int i = 0; i < s.length(); i += 2) {
			result = result.concat(typeOneCharDecrypt(s.subSequence(i, i+2).toString()));
		}
		return result;
	}

	private static String typeOneCharEncrypt(char c) {
		switch(c) {
			case '1':
				return "00";
			case '2':
				return "01";
			case '3':
				return "02";
			case '4':
				return "03";
			case '5':
				return "04";
			case '6':
				return "05";
			case '7':
				return "06";
			case '8':
				return "07";
			case '9':
				return "08";
			case '0':
				return "09";
			case 'z':
			case 'Z':
				return "10";
			case 'a':
			case 'A':
				return "11";
			case 'b':
			case 'B':
				return "12";
			case 'c':
			case 'C':
				return "13";
			case 'd':
			case 'D':
				return "14";
			case 'e':
			case 'E':
				return "15";
			case '!':
				return "16";
			case '@':
				return "17";
			case '#':
				return "18";
			case '$':
				return "19";
			case '%':
				return "20";
			case 'f':
			case 'F':
				return "21";
			case 'g':
			case 'G':
				return "22";
			case 'h':
			case 'H':
				return "23";
			case 'i':
			case 'I':
				return "24";
			case 'j':
			case 'J':
				return "25";
			case '^':
				return "26";
			case '&':
				return "27";
			case '*':
				return "28";
			case '(':
				return "29";
			case ')':
				return "30";
			case 'k':
			case 'K':
				return "31";
			case 'l':
			case 'L':
				return "32";
			case 'm':
			case 'M':
				return "33";
			case 'n':
			case 'N':
				return "34";
			case 'o':
			case 'O':
				return "35";
			case '-':
				return "36";
			case '=':
				return "37";
			case '+':
				return "38";
			case '\\':
				return "39";
			case '|':
				return "40";
			case 'p':
			case 'P':
				return "41";
			case 'q':
			case 'Q':
				return "42";
			case 'r':
			case 'R':
				return "43";
			case 's':
			case 'S':
				return "44";
			case 't':
			case 'T':
				return "45";
			case '/':
				return "46";
			case '.':
				return "47";
			case ',':
				return "48";
			case '<':
				return "49";
			case '>':
				return "50";
			case 'u':
			case 'U':
				return "51";
			case 'v':
			case 'V':
				return "52";
			case 'w':
			case 'W':
				return "53";
			case 'x':
			case 'X':
				return "54";
			case 'y':
			case 'Y':
				return "55";
			default:
				return "56";
		}
	}

	private static String typeOneCharDecrypt(String num) {
		if (num.equals("00"))
			return "1";
		if (num.equals("01"))
			return "2";
		if (num.equals("02"))
			return "3";
		if (num.equals("03"))
			return "4";
		if (num.equals("04"))
			return "5";
		if (num.equals("05"))
			return "6";
		if (num.equals("06"))
			return "7";
		if (num.equals("07"))
			return "8";
		if (num.equals("08"))
			return "9";
		if (num.equals("09"))
			return "0";
		if (num.equals("10"))
			return "Z";
		if (num.equals("11"))
			return "A";
		if (num.equals("12"))
			return "B";
		if (num.equals("13"))
			return "C";
		if (num.equals("14"))
			return "D";
		if (num.equals("15"))
			return "E";
		if (num.equals("16"))
			return "!";
		if (num.equals("17"))
			return "@";
		if (num.equals("18"))
			return "#";
		if (num.equals("19"))
			return "$";
		if (num.equals("20"))
			return "%";
		if (num.equals("21"))
			return "F";
		if (num.equals("22"))
			return "G";
		if (num.equals("23"))
			return "H";
		if (num.equals("24"))
			return "I";
		if (num.equals("25"))
			return "J";
		if (num.equals("26"))
			return "^";
		if (num.equals("27"))
			return "&";
		if (num.equals("28"))
			return "*";
		if (num.equals("29"))
			return "(";
		if (num.equals("30"))
			return ")";
		if (num.equals("31"))
			return "K";
		if (num.equals("32"))
			return "L";
		if (num.equals("33"))
			return "M";
		if (num.equals("34"))
			return "N";
		if (num.equals("35"))
			return "O";
		if (num.equals("36"))
			return "-";
		if (num.equals("37"))
			return "=";
		if (num.equals("38"))
			return "+";
		if (num.equals("39"))
			return "\\";
		if (num.equals("40"))
			return "|";
		if (num.equals("41"))
			return "P";
		if (num.equals("42"))
			return "Q";
		if (num.equals("43"))
			return "R";
		if (num.equals("44"))
			return "S";
		if (num.equals("45"))
			return "T";
		if (num.equals("46"))
			return "/";
		if (num.equals("47"))
			return ".";
		if (num.equals("48"))
			return ",";
		if (num.equals("49"))
			return "<";
		if (num.equals("50"))
			return ">";
		if (num.equals("51"))
			return "U";
		if (num.equals("52"))
			return "V";
		if (num.equals("53"))
			return "W";
		if (num.equals("54"))
			return "X";
		if (num.equals("55"))
			return "Y";
		return " ";
	}

	private static String[] blockify(String plaintext) {
		String[] result;
		if (plaintext.length() % Rijndael.BLOCK_SIZE != 0)
			result = new String[plaintext.length()/Rijndael.BLOCK_SIZE + 1];
		else
			result = new String[plaintext.length()/Rijndael.BLOCK_SIZE];
		for (int i = 0; i < result.length; i++) {
			if (plaintext.length() < Rijndael.BLOCK_SIZE) {
				String block = plaintext;
				plaintext = "";
				while (block.length() < Rijndael.BLOCK_SIZE) {
					block = block.concat(" ");
				}
				result[i] = block;
			} else {
				String block = plaintext.substring(0, Rijndael.BLOCK_SIZE);
				plaintext = plaintext.substring(Rijndael.BLOCK_SIZE);
				result[i] = block;
			}
		}
		return result;
	}

	private static String[] blockifyBinary(String plaintext) {
		String[] result;
		if ((plaintext.length()/8) % Rijndael.BLOCK_SIZE != 0)
			result = new String[(plaintext.length()/8)/Rijndael.BLOCK_SIZE + 1];
		else
			result = new String[(plaintext.length()/8)/Rijndael.BLOCK_SIZE];
		for (int i = 0; i < result.length; i++) {
			if ((plaintext.length()/8) < Rijndael.BLOCK_SIZE) {
				String block = plaintext;
				plaintext = "";
				while ((block.length()/8) < Rijndael.BLOCK_SIZE) {
					block = block.concat("00000000");
				}
				result[i] = block;
			} else {
				String block = plaintext.substring(0, 8*Rijndael.BLOCK_SIZE);
				plaintext = plaintext.substring(8*Rijndael.BLOCK_SIZE);
				result[i] = block;
			}
		}
		return result;
	}

	private static byte[] stringToBytes(String plaintext) {
		byte[] bytes = new byte[plaintext.length()];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = Byte.decode(Integer.toString(plaintext.charAt(i)));
		}
		return bytes;
	}

	private static byte[] binaryToBytes(String plaintext) {
		byte[] bytes = new byte[plaintext.length()/8];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = binaryToByte(plaintext.substring(i*8, i*8+8));
		}
		return bytes;
	}

	private static String byteToBinary(byte b) {
		char[] bits = byteToBasicBinary(b);
		if (b < 0) {
			bits[0] = '1';
			for (int i = 7; i > 0; i--) { // invert bits
				if (bits[i] == '0')
					bits[i] = '1';
				else
					bits[i] = '0';
			}
			for (int i = 7; i > 0; i--) { // add 1
				if (bits[i] == '0') {
					bits[i] = '1';
					break;
				} else
					bits[i] = '0';
			}
		}
		return new String(bits);
	}

	private static char[] byteToBasicBinary(byte b) {
		char[] bits = new char[8];
		int num = new Byte(b).intValue();
		if (num < 0)
			num = num * (-1);
		for (int i = 7; i > -1; i--) {
			if ((num % 2) == 0)
				bits[i] = '0';
			else
				bits[i] = '1';
			num = num/2;
		}
		return bits;
	}

	private static byte binaryToByte(String binary) {
		char[] bits = binary.toCharArray();
		boolean neg = false;

		if (bits[0] == '1') {
			neg = true;
			for (int i = 7; i > -1; i--) { // invert bits
				if (bits[i] == '0')
					bits[i] = '1';
				else
					bits[i] = '0';
			}
			for (int i = 7; i > -1; i--) { // add 1
				if (bits[i] == '0') {
					bits[i] = '1';
					break;
				} else
					bits[i] = '0';
			}
		}

		int x = 0;
		for (int i = 7; i > -1; i--) {
			if (bits[i] == '1') {
				x = x + exp(2, 7-i);
			}
		}
		if (neg)
			x = x * (-1);

		return Byte.decode(Integer.toString(x));
	}

	private static int exp(int base, int exponent) {
		if (exponent == 0)
			return 1;
		else
			return (base * exp(base, exponent-1));
	}

	private static String removeEndingSpace(String s) {
		int index = s.length()-1;
		while (s.charAt(index) == ' ' && index > -1)
			index--;
		return s.substring(0, index+1);
	}
}