package displays;

public class CashierDisplay extends Display{
	
    @Override
    public void menuDisplay() {
		System.out.println("                     a88888b.                   dP       oo                                       ");
		System.out.println("                    d8'   `88                   88                                                ");
		System.out.println("                    88        .d8888b. .d8888b. 88d888b. dP .d8888b. 88d888b.                     ");
		System.out.println("                    88        88'  `88 Y8ooooo. 88'  `88 88 88ooood8 88'  `88                     ");
		System.out.println("                    Y8.   .88 88.  .88       88 88    88 88 88.  ... 88                           ");
		System.out.println("                     Y88888P' `88888P8 `88888P' dP    dP dP `88888P' dP                           ");
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃ ┌────────────────────────────────────────────────────────────────────────────────────────────┐ ┃");
		System.out.println("┃ │ --------┏━━━━━━━━━━━━━━━━━━┓-------┏━━━━━━━━━━━━━━━━━━┓-------┏━━━━━━━━━━━━━━━━━━┓-------- │ ┃");
		System.out.println("┃ │ --------┃ 1. View pending  ┃-------┃    2. Manage     ┃-------┃    3. Manage     ┃-------- │ ┃");
		System.out.println("┃ │ --------┃     payments     ┃-------┃ Pending Requests ┃-------┃      History     ┃-------- │ ┃");
		System.out.println("┃ │ --------┗━━━━━━━━━━━━━━━━━━┛-------┗━━━━━━━━━━━━━━━━━━┛-------┗━━━━━━━━━━━━━━━━━━┛-------- │ ┃");
		System.out.println("┃ │ ---------------------┏━━━━━━━━━━━━━━━━━━┓--------┏━━━━━━━━━━━━━━━━━━┓--------------------- │ ┃");
		System.out.println("┃ │ ---------------------┃  4. Respond to   ┃--------┃    5. Log-out    ┃--------------------- │ ┃");
		System.out.println("┃ │ ---------------------┃      Concern     ┃--------┃                  ┃--------------------- │ ┃");
		System.out.println("┃ │ ---------------------┗━━━━━━━━━━━━━━━━━━┛--------┗━━━━━━━━━━━━━━━━━━┛--------------------- │ ┃");
		System.out.println("┃ └────────────────────────────────────────────────────────────────────────────────────────────┘ ┃");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	}
	
	public static void pendingDisplay() {
		System.out.println("    .oPYo.                   8  o                 .oPYo.                                      o         ");
		System.out.println("    8    8                   8                    8   `8                                      8         ");
		System.out.println("   o8YooP' .oPYo. odYo. .oPYo8 o8 odYo. .oPYo.   o8YooP' .oPYo. .oPYo. o    o .oPYo. .oPYo.  o8P .oPYo. ");
		System.out.println("    8      8oooo8 8' `8 8    8  8 8' `8 8    8    8   `b 8oooo8 8    8 8    8 8oooo8 Yb..     8  Yb..   ");
		System.out.println("    8      8.     8   8 8    8  8 8   8 8    8    8    8 8.     8    8 8    8 8.       'Yb.   8    'Yb. ");
		System.out.println("    8      `Yooo' 8   8 `YooP'  8 8   8 `YooP8    8    8 `Yooo' `YooP8 `YooP' `Yooo' `YooP'   8  `YooP' ");
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━8━━━━━━━━━━━━━━━━━━━━━━━8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃ ┌───────────────────────────────────────ooP'───────────────────────8─────────────────────────────────┐ ┃");
		System.out.println("┃ └────────────────────────────────────────────────────────────────────────────────────────────────────┘ ┃");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	}
	public static void CashierHistoryMenuDisplay() {
		System.out.println("     .oPYo.               8       o                 o    o  o          o                          ");
		System.out.println("     8    8               8                         8    8             8                          ");
		System.out.println("     8      .oPYo. .oPYo. 8oPYo. o8 .oPYo. oPYo.   o8oooo8 o8 .oPYo.  o8P .oPYo. oPYo. o    o     ");
		System.out.println("     8      .oooo8 Yb..   8    8  8 8oooo8 8  `'    8    8  8 Yb..     8  8    8 8  `' 8    8     ");
		System.out.println("     8    8 8    8   'Yb. 8    8  8 8.     8        8    8  8   'Yb.   8  8    8 8     8    8     ");
		System.out.println("     `YooP' `YooP8 `YooP' 8    8  8 `Yooo' 8        8    8  8 `YooP'   8  `YooP' 8     `YooP8     ");
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━8━━━━┓");
		System.out.println("┃ ┌──────────────────────────────────────────────────────────────────────────────────────ooP'──┐ ┃");
		System.out.println("┃ └────────────────────────────────────────────────────────────────────────────────────────────┘ ┃");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	}
}
