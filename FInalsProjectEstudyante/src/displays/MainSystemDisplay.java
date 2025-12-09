package displays;

public class MainSystemDisplay extends Display{

    @Override
    public void menuDisplay() {
		System.out.println(ANSI_CYAN +" 88888888b          .d88888b    dP                  dP                              dP            ");
		System.out.println(ANSI_CYAN +" 88                 88.    \"'   88                  88                              88           ");
		System.out.println(ANSI_CYAN +"a88aaaa             `Y88888b. d8888P dP    dP .d888b88 dP    dP .d8888b. 88d888b. d8888P .d8888b. ");
		System.out.println(ANSI_CYAN +" 88        88888888       `8b   88   88    88 88'  `88 88    88 88'  `88 88'  `88   88   88ooood8 ");
		System.out.println(ANSI_CYAN +" 88                 d8'   .8P   88   88.  .88 88.  .88 88.  .88 88.  .88 88    88   88   88.  ... ");
		System.out.println(ANSI_CYAN +" 88888888P           Y88888P    dP   `88888P' `88888P8 `8888P88 `88888P8 dP    dP   dP   `88888P' ");
		System.out.println(ANSI_BLUE +"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"+ANSI_CYAN+"88"+ANSI_BLUE+"━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println(ANSI_BLUE +"┃ ┌─────────────────────────────────────────────────────"+ANSI_CYAN+"d8888P"+ANSI_BLUE+ "─────────────────────────────────┐ ┃");
		System.out.println(ANSI_BLUE +"┃ │"+ANSI_WHITE+". ݁₊ ⊹ . ݁ ⟡ ݁ .---"+ANSI_CYAN+"┏━━━━━━━━━━━━┓"+ANSI_WHITE+"-------"+ANSI_CYAN+"┏━━━━━━━━━━━━┓"+ANSI_WHITE+"-------"+ANSI_CYAN+"┏━━━━━━━━━━━━┓"+ANSI_WHITE+"---. ݁₊ ⊹ . ݁ ⟡ ݁ ."+ANSI_BLUE+"│ ┃");
		System.out.println(ANSI_BLUE +"┃ │"+ANSI_WHITE+". ݁₊ ⊹ . ݁ ⟡ ݁ .---"+ANSI_CYAN+"┃ 1. Sign-up ┃"+ANSI_WHITE+"-------"+ANSI_CYAN+"┃ 2. Log-in  ┃"+ANSI_WHITE+"-------"+ANSI_CYAN+"┃  3. Exit   ┃"+ANSI_WHITE+"---. ݁₊ ⊹ . ݁ ⟡ ݁ ."+ANSI_BLUE+"│ ┃");
		System.out.println(ANSI_BLUE +"┃ │"+ANSI_WHITE+". ݁₊ ⊹ . ݁ ⟡ ݁ .---"+ANSI_CYAN+"┗━━━━━━━━━━━━┛"+ANSI_WHITE+"-------"+ANSI_CYAN+"┗━━━━━━━━━━━━┛"+ANSI_WHITE+"-------"+ANSI_CYAN+"┗━━━━━━━━━━━━┛"+ANSI_WHITE+"---. ݁₊ ⊹ . ݁ ⟡ ݁ ."+ANSI_BLUE+"│ ┃");
		System.out.println(ANSI_BLUE +"┃ └────────────────────────────────────────────────────────────────────────────────────────────┘ ┃");
		System.out.println(ANSI_BLUE +"┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"+ ANSI_CYAN );
	}
	public static void signUpDisplay() {
		System.out.println(ANSI_CYAN +"                             .oPYo.  o                     o                                      ");
		System.out.println(ANSI_CYAN +"                             8                                                                    ");
		System.out.println(ANSI_CYAN +"                             `Yooo. o8 .oPYo. odYo.       o8 odYo.                                ");
		System.out.println(ANSI_CYAN +"                                 `8  8 8    8 8' `8 ooooo  8 8' `8                                ");
		System.out.println(ANSI_CYAN +"                                  8  8 8    8 8   8        8 8   8                                ");
		System.out.println(ANSI_CYAN +"                             `YooP'  8 `YooP8 8   8        8 8   8                                ");
		System.out.println(ANSI_BLUE +"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"+ANSI_CYAN+"8"+ANSI_BLUE+"━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println(ANSI_BLUE +"┃ ┌──────────────────────────────────────"+ANSI_CYAN+"ooP'"+ANSI_BLUE+"──────────────────────────────────────────────────┐ ┃");
		System.out.println(ANSI_BLUE +"┃ └────────────────────────────────────────────────────────────────────────────────────────────┘ ┃");
		System.out.println(ANSI_BLUE +"┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"+ANSI_CYAN);
	}
	public static void loginDisplay() {
		System.out.println(ANSI_CYAN +"                                o                          o                                       ");
		System.out.println(ANSI_CYAN +"                                8                                                                  ");
		System.out.println(ANSI_CYAN +"                                8     .oPYo. .oPYo.       o8 odYo.                                 ");
		System.out.println(ANSI_CYAN +"                                8     8    8 8    8 ooooo  8 8' `8                                 ");
		System.out.println(ANSI_CYAN +"                                8     8    8 8    8        8 8   8                                 ");
		System.out.println(ANSI_CYAN +"                                8oooo `YooP' `YooP8        8 8   8                                 ");
		System.out.println(ANSI_BLUE +"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"+ANSI_CYAN+"8"+ANSI_BLUE+"━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println(ANSI_BLUE +"┃ ┌────────────────────────────────────────────"+ANSI_CYAN+"ooP'"+ANSI_BLUE+"─────────────────────────────────────────────┐ ┃");
		System.out.println(ANSI_BLUE +"┃ └────────────────────────────────────────────────────────────────────────────────────────────┘ ┃");
		System.out.println(ANSI_BLUE +"┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"+ANSI_CYAN);
	}
}
