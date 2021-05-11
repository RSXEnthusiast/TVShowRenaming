import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TVShowRenaming {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		HashMap<String, String> info = new HashMap<String, String>();
		File[] listOfSeasons = new File[0];
		do {
			scanner = new Scanner(System.in);
			info = getInfo(scanner);
			File folder = new File(info.get("showTitle"));
			listOfSeasons = folder.listFiles();
			System.out.println();
		} while (!printExampleEpisode(scanner, listOfSeasons[0], info));
		System.out.println();
		scanner = new Scanner(info.get("seasonsToDo"));
		scanner.useDelimiter(",");
		for (int i = 0; i < listOfSeasons.length; i++) {
			if (listOfSeasons[i].isDirectory()) {
				if (info.get("isFullShow").equals("N")) {
					try {
						renameEpisodes(listOfSeasons[i], scanner.nextInt(), info);
					} catch (NoSuchElementException e) {
						i = listOfSeasons.length;
					}
				} else {
					renameEpisodes(listOfSeasons[i], i + 1, info);
				}
			} else {
				System.out.println("Skipped \"" + listOfSeasons[i].getName() + "\" as it isn't a directory.");
			}
		}
	}

	private static boolean printExampleEpisode(Scanner scanner, File seasonDirectory, HashMap<String, String> info) {
		System.out.println("The first episode will be changed from:");
		System.out.println(seasonDirectory.listFiles()[0].getName());
		System.out.println("to:");
		System.out.println(getNewName(seasonDirectory.listFiles()[0], 1, info.get("showTitle") + " - SXXE", info));
		while (true) {
			System.out.print("\nWould you like to continue? Enter Y/N: ");
			switch (scanner.next().toUpperCase()) {
			case "Y":
				return true;
			case "N":
				return false;
			default:
				System.out.println("Please make a valid entry!\n");
			}
		}
	}

	private static HashMap<String, String> getInfo(Scanner scanner) {
		HashMap<String, String> result = new HashMap<String, String>();
		String answer = "";
		// Directory/title
		System.out.print("Enter parent directory/title of TV Show (they need to be the same): ");
		result.put("showTitle", scanner.nextLine());
		// Episode Title Information
		while (true) {
			System.out.print("Is there an episode specific title that needs to be saved? Enter Y/N: ");
			answer = scanner.next().toUpperCase();
			if (answer.equals("Y")) {
				System.out
						.print("Enter how many characters are before the episode title of episodes 1-9 respectively: ");
				result.put("isEpisodeTitle", answer);
				result.put("charsBeforeEpisodeTitle", scanner.next());
				answer = "";
				boolean loopIsDone = false;
				while (!loopIsDone) {
					System.out.print("Does this number increase by 1 when the show reaches episode 10? Enter Y/N: ");
					switch (scanner.next().toUpperCase()) {
					case "Y":
						result.put("extraCharE10+", "Y");
						loopIsDone = true;
						break;
					case "N":
						result.put("extraCharE10+", "N");
						loopIsDone = true;
						break;
					default:
						System.out.println("Enter a valid selection");
						break;
					}
				}
				break;
			} else if (answer.equals("N")) {
				result.put("isEpisodeTitle", answer);
				result.put("charsBeforeEpisodeTitle", "0");
				answer = "";
				break;
			} else {
				System.out.println("Please enter a valid selection.\n");
			}
		}
		while (true) {
			System.out.print("Are there characters after the episode title? Enter Y/N: ");
			answer = scanner.next().toUpperCase();
			if (answer.equals("Y")) {
				System.out.print("Enter how many characters need to be removed after the episode title: ");
				result.put("postEpisodeTitleChars", scanner.next());
				break;
			} else if (answer.equals("N")) {
				result.put("postEpisodeTitleChars", "0");
				break;
			} else {
				System.out.println("Please enter a valid selection.\n");
			}
		}
		answer = "";
		while (true) {
			System.out.print(
					"Are there any characters in the episode title that need to be replaced with spaces? Enter Y/N: ");
			answer = scanner.next().toUpperCase();
			if (answer.equals("Y")) {
				boolean flag = false;
				while (!flag) {
					System.out.print("Enter the characters that need to be replaced, sepearted by commas: ");
					String temp = scanner.next();
					Scanner lineScan = new Scanner(temp);
					lineScan.useDelimiter(",");
					while (lineScan.hasNext()) {
						String temp2 = lineScan.next();
						if (temp2.length() == 1) {
							flag = true;
						} else {
							System.out.println("Each character should only be of length 1. Try again.");
							flag = false;
							break;
						}
					}
					result.put("charsToRemove", temp);
					lineScan.close();
				}
				break;
			} else if (answer.equals("N")) {
				break;
			} else {
				System.out.println("Please make a valid selection.\n");
			}
		}
		while (true) {
			System.out.print("Do the seasons in the folder start at season 1 and go up consecutively? Enter Y?N: ");
			answer = scanner.next().toUpperCase();
			result.put("isFullShow", answer);
			if (answer.equals("N")) {
				boolean flag = false;
				while (!flag) {
					System.out.print("Enter a comma seperated list of the seasons in the folder: ");
					String temp = scanner.next();
					Scanner lineScan = new Scanner(temp);
					lineScan.useDelimiter(",");
					while (lineScan.hasNext()) {
						try {
							lineScan.nextInt();
							flag = true;
						} catch (InputMismatchException e) {
							System.out.println("Each input should be an integer.");
							flag = false;
							break;
						}
					}
					result.put("seasonsToDo", temp);
					lineScan.close();
				}
				break;
			} else if (answer.equals("Y")) {
				break;
			} else {
				System.out.println("Please make a valid selection.\n");
			}
		}
		return result;
	}

	private static void renameEpisodes(File seasonDirectory, int seasonNumber, HashMap<String, String> info) {
		File[] episodes = seasonDirectory.listFiles();
		String fileNameTemplate = info.get("showTitle") + " - S";
		if (seasonNumber < 10) {
			fileNameTemplate += "0";
		}
		fileNameTemplate += seasonNumber + "E";
		for (int i = 0; i < episodes.length; i++) {
			renameEpisode(seasonDirectory.getAbsolutePath(), episodes[i], i + 1, fileNameTemplate, info);
		}
	}

	private static void renameEpisode(String path, File file, int episodeNumber, String fileNameTemplate,
			HashMap<String, String> info) {
		fileNameTemplate = getNewName(file, episodeNumber, fileNameTemplate, info);
		File newName = new File(path + "/" + fileNameTemplate);
		String oldName = file.getName();
		if (file.renameTo(newName)) {
			System.out.println("Renamed \"" + oldName + "\" to \"" + newName.getName() + "\"");
		} else {
			System.out.println("Renaming of \"" + oldName + "\" to \"" + newName.getName() + "\" failed!");
		}
	}

	private static String getNewName(File file, int episodeNumber, String fileName, HashMap<String, String> info) {
		if (episodeNumber < 10) {
			fileName += "0";
		}
		fileName += episodeNumber;
		if (info.get("isEpisodeTitle").equals("Y")) {
			fileName += " - ";
			String next = "";
			if (info.get("extraCharE10+").equals("N") || episodeNumber < 10) {
				next = file.getName().substring(Integer.valueOf(info.get("charsBeforeEpisodeTitle")),
						file.getName().length() - 4 - Integer.valueOf(info.get("postEpisodeTitleChars")));
			} else {
				next = file.getName().substring(Integer.valueOf(info.get("charsBeforeEpisodeTitle")) + 1,
						file.getName().length() - 4 - Integer.valueOf(info.get("postEpisodeTitleChars")));
			}
			try {
				Scanner scanString = new Scanner(info.get("charsToRemove"));
				scanString.useDelimiter(",");
				while (scanString.hasNext()) {
					next = next.replace(scanString.next(), " ");
				}
			} catch (NullPointerException e) {
				// empty catch - no chars to remove
			}
			while (next.charAt(next.length() - 1) == ' ') {
				next = next.substring(0, next.length() - 2);
			}
			fileName += next;
		}
		fileName += file.getName().substring(file.getName().length() - 4);
		return fileName;
	}
}
