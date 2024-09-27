package top.auspice.utils;

import org.bukkit.ChatColor;
import top.auspice.config.compilers.message.pieces.MessagePiece;
import top.auspice.utils.string.Strings;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;

public final class ColorUtils {
    private static final byte[] a;

    public ColorUtils() {
    }

    public static Color hex(String var0) {
        try {
            int var1;
            boolean var2 = (var1 = (var0 = Strings.deleteWhitespace(Strings.remove(var0, '#'))).length()) == 4 || var1 == 8;
            if (var1 != 3 && var1 != 4) {
                if (var1 != 6 && var1 != 8) {
                    return null;
                }
            } else {
                var0 = shortHexToLongHex(var0);
            }

            if (var1 == 8) {
                var0 = var0.substring(6, 8) + var0.substring(0, 6);
            }

            var0 = var0;
            var1 = 0;
            int var3 = var0.length();
            int var4 = 0;

            Integer var10000;
            while(true) {
                if (var1 >= var3) {
                    var10000 = -var4;
                    break;
                }

                var4 <<= 4;
                char var5;
                if ((var5 = var0.charAt(var1++)) >= a.length) {
                    var10000 = null;
                    break;
                }

                byte var8;
                if ((var8 = a[var5]) == -1) {
                    var10000 = null;
                    break;
                }

                var4 -= var8;
            }

            Integer var7 = var10000;
            return var10000 == null ? null : new Color(var7, var2);
        } catch (NumberFormatException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static String toString(Color var0) {
        return "{ R:" + var0.getRed() + " | G:" + var0.getGreen() + " | B:" + var0.getBlue() + " | A:" + var0.getAlpha() + " }";
    }

    public static int getRGB(Color var0) {
        return (var0.getRed() & 255) << 16 | (var0.getGreen() & 255) << 8 | var0.getBlue() & 255;
    }

    public static Color parseColor(String var0) {
        Color var1;
        return (var1 = hex(var0)) == null ? rgb(var0) : var1;
    }

    public static String shortHexToLongHex(String var0) {
        char var1 = var0.charAt(0);
        char var2 = var0.charAt(1);
        char var3 = var0.charAt(2);
        char var4 = var0.length() == 4 ? var0.charAt(3) : 70;
        return String.valueOf(var4) + var4 + var1 + var1 + var2 + var2 + var3 + var3;
    }

    public static int toHex(Color var0) {
        return var0.getRed() << 16 | var0.getGreen() << 8 | var0.getBlue();
    }

    public static int getHue(@Nonnull Color var0) {
        int var1 = var0.getRed();
        int var2 = var0.getGreen();
        int var5 = var0.getBlue();
        float var3 = (float)Math.min(Math.min(var1, var2), var5);
        float var4 = (float)Math.max(Math.max(var1, var2), var5);
        if (var3 == var4) {
            return 0;
        } else {
            var3 = var4 - var3;
            float var6;
            if (var4 == (float)var1) {
                var6 = (float)(var2 - var5) / var3;
            } else if (var4 == (float)var2) {
                var6 = 2.0F + (float)(var5 - var1) / var3;
            } else {
                var6 = 4.0F + (float)(var1 - var2) / var3;
            }

            if ((var6 *= 60.0F) < 0.0F) {
                var6 += 360.0F;
            }

            return Math.round(var6);
        }
    }

    public static float[] getHSB(Color var0) {
        float[] var1;
        (var1 = Color.RGBtoHSB(var0.getRed(), var0.getGreen(), var0.getBlue(), (float[])null))[0] = var1[0] * 360.0F;
        var1[1] *= 100.0F;
        var1[2] *= 100.0F;
        return var1;
    }

    public static String toHexString(Color var0) {
        Objects.requireNonNull(var0, "Cannot convert null color to hex");
        return toHexString(var0.getRGB());
    }

    public static String toHexString(int var0) {
        return String.format("%06x", var0 & 16777215);
    }

    public static int legacyColorToHex(ChatColor var0) {
        return switch (var0) {
            case WHITE -> 16777215;
            case BLACK -> 0;
            case DARK_BLUE -> 170;
            case DARK_GREEN -> 43520;
            case DARK_AQUA -> 43690;
            case DARK_RED -> 11141120;
            case DARK_PURPLE -> 11141290;
            case DARK_GRAY -> 5592405;
            case GRAY -> 11184810;
            case GOLD -> 16755200;
            case BLUE -> 5592575;
            case GREEN -> 5635925;
            case AQUA -> 5636095;
            case RED -> 16733525;
            case LIGHT_PURPLE -> 16733695;
            case YELLOW -> 16777045;
            default -> throw new IllegalArgumentException("Specified chat color is not a color: " + var0);
        };
    }

    public static Color legacyColorToAwt(ChatColor var0) {
        return new Color(legacyColorToHex(var0));
    }

    public static Color rgb(String str) {
        java.util.List<String> compiled;
        if ((compiled = Strings.cleanSplitManaged(str, str.contains(",") ? ',' : ' ')).size() < 3) {
            return null;
        } else {
            try {
                int r = Integer.parseInt(compiled.get(0));
                int g = Integer.parseInt(compiled.get(1));
                int b = Integer.parseInt(compiled.get(2));
                int a = compiled.size() > 3 ? Integer.parseInt(compiled.get(3)) : 255;
                return new Color(r, g, b, a);
            } catch (NumberFormatException var4) {
                return null;
            }
        }
    }

    public static java.util.List<MessagePiece> gradient(List<MessagePiece> var0, Color var1, Color var2) {
        int var3 = 0;

        for (MessagePiece messagePiece : var0) {
            MessagePiece var5;
            if ((var5 = messagePiece) instanceof MessagePiece.Plain) {
                var3 += var5.length();
            } else if (!(var5 instanceof MessagePiece.Color)) {
                throw new IllegalArgumentException("Disallowed piece in gradient color: " + var5);
            }
        }

        double var18 = Math.abs((double)(var1.getRed() - var2.getRed()) / (double)var3);
        double var6 = Math.abs((double)(var1.getGreen() - var2.getGreen()) / (double)var3);
        double var8 = Math.abs((double)(var1.getBlue() - var2.getBlue()) / (double)var3);
        if (var1.getRed() > var2.getRed()) {
            var18 = -var18;
        }

        if (var1.getGreen() > var2.getGreen()) {
            var6 = -var6;
        }

        if (var1.getBlue() > var2.getBlue()) {
            var8 = -var8;
        }

        ArrayList<MessagePiece> var16 = new ArrayList<>(var3 * 3);
        var1 = new Color(var1.getRGB());
        boolean var17 = false;
        String var10 = null;
        int var11 = 0;
        Iterator<MessagePiece> var14 = var0.iterator();

        while(true) {
            while(var14.hasNext()) {
                MessagePiece var12;
                int var19;
                if ((var12 = var14.next()) instanceof MessagePiece.Plain) {
                    if (var10 == null) {
                        var10 = ((MessagePiece.Plain)var12).getMessage();
                    }

                    if (var17) {
                        var17 = false;
                        continue;
                    }

                    var19 = var10.charAt(var11++);
                    var16.add(new MessagePiece.HexColor(var1));
                    var16.add(new MessagePiece.Plain(String.valueOf((char)var19)));
                } else if (var12 instanceof MessagePiece.Color) {
                    if (var12 instanceof MessagePiece.SimpleColor) {
                        var17 = ((MessagePiece.SimpleColor)var12).getColor().isFormat();
                    }

                    var16.add(var12);
                }

                var19 = (int)Math.round((double)var1.getRed() + var18);
                int var13 = (int)Math.round((double)var1.getGreen() + var6);
                int var15 = (int)Math.round((double)var1.getBlue() + var8);
                if (var19 > 255) {
                    var19 = 255;
                }

                if (var19 < 0) {
                    var19 = 0;
                }

                if (var13 > 255) {
                    var13 = 255;
                }

                if (var13 < 0) {
                    var13 = 0;
                }

                if (var15 > 255) {
                    var15 = 255;
                }

                if (var15 < 0) {
                    var15 = 0;
                }

                var1 = new Color(var19, var13, var15);
            }

            return var16;
        }
    }

    public static double distance(Color var0, Color var1) {
        int var2 = var0.getRed();
        int var3 = var1.getRed();
        int var4 = var2 + var3 >> 1;
        var2 -= var3;
        var3 = var0.getGreen() - var1.getGreen();
        int var5 = var0.getBlue() - var1.getBlue();
        return Math.sqrt(((var4 + 512) * var2 * var2 >> 8) + 4 * var3 * var3 + ((767 - var4) * var5 * var5 >> 8));
    }

    public static Color mixColors(Color... var0) {
        float var1 = 1.0F / (float)var0.length;
        int var2 = 0;
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        int var6 = (var0).length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Color var8 = var0[var7];
            var2 = (int)((float)var2 + (float)var8.getRed() * var1);
            var3 = (int)((float)var3 + (float)var8.getGreen() * var1);
            var4 = (int)((float)var4 + (float)var8.getBlue() * var1);
            var5 = (int)((float)var5 + (float)var8.getAlpha() * var1);
        }

        return new Color(var2, var3, var4, var5);
    }

    static {
        Arrays.fill(a = new byte[103], (byte)-1);

        int var0;
        for(var0 = 48; var0 <= 57; ++var0) {
            a[var0] = (byte)(var0 - 48);
        }

        for(var0 = 65; var0 <= 70; ++var0) {
            a[var0] = (byte)(var0 - 65 + 10);
        }

        for(var0 = 97; var0 <= 102; ++var0) {
            a[var0] = (byte)(var0 - 97 + 10);
        }

    }
}
