package top.auspice.utils.time;



import org.checkerframework.dataflow.qual.Pure;
import top.auspice.utils.MathUtils;
import top.auspice.utils.string.Strings;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.annotation.Nonnull;


@SuppressWarnings("unused")
public final class TimeUtils {
    private static final HashMap<String, Function<Long, Long>> a = new HashMap<>(18);
    public static ZoneId TIME_ZONE = ZoneId.systemDefault();
    public static final DateTimeFormatter DATE_TIME_FORMAT;
    public static final DateTimeFormatter DATE_FORMAT;
    public static final DateTimeFormatter TIME_FORMAT;

    public TimeUtils() {
    }

    public static void validateUnixTime(long var0) {
        if (var0 < 1588316400L) {
            throw new IllegalArgumentException("Possible invalid timestamp: " + var0);
        }
    }

    @Nonnull
    public static StringBuilder getDateAndTime(TemporalAccessor var0) {
        return formatTime(DATE_TIME_FORMAT, var0, 19);
    }

    @Nonnull
    public static StringBuilder formatTime(DateTimeFormatter var0, TemporalAccessor var1, int var2) {
        StringBuilder var3 = new StringBuilder(var2);
        var0.formatTo(var1, var3);
        return var3;
    }

    @Nonnull
    public static StringBuilder getDateAndTime(long var0) {
        return getDateAndTime(Instant.ofEpochMilli(var0));
    }

    @Nonnull
    public static ZonedDateTime now() {
        return ZonedDateTime.now(TIME_ZONE);
    }

    @Nonnull
    @Pure
    public static ZonedDateTime epoch() {
        return ZonedDateTime.ofInstant(Instant.EPOCH, TIME_ZONE);
    }

    @Nonnull
    public static StringBuilder getDateAndTime() {
        return getDateAndTime(LocalDateTime.now());
    }

    @Nonnull
    public static StringBuilder getTime() {
        return getTime(LocalTime.now());
    }

    @Nonnull
    public static StringBuilder getTime(TemporalAccessor var0) {
        return formatTime(TIME_FORMAT, var0, 8);
    }

    @Nonnull
    public static StringBuilder getTime(long var0) {
        return getTime(Instant.ofEpochMilli(var0));
    }

    public static long afterNow(Duration var0) {
        return var0.plusMillis(System.currentTimeMillis()).toMillis();
    }

    @Nonnull
    public static StringBuilder getDate(long var0) {
        return getDate(Instant.ofEpochMilli(var0));
    }

    @Nonnull
    public static StringBuilder getDate(TemporalAccessor var0) {
        return formatTime(DATE_FORMAT, var0, 10);
    }

    @Nonnull
    public static StringBuilder getDate() {
        return getDate(LocalDateTime.now());
    }

    public static long getTimeUntil(CharSequence var0, String var1, ChronoUnit var2) {
        LocalDateTime var10 = LocalDateTime.now(ZoneId.of(var1));
        LocalDateTime var9 = LocalTime.parse(var0).atDate(var10.toLocalDate());
        long var7;
        if ((var7 = var2.between(var10, var9)) <= 0L) {
            var9 = var9.plusDays(1L);
            var7 = var2.between(var10, var9);
        }

        return var7 * 20L;
    }

    public static Duration getTimeUntilTomrrow(ZoneId var0) {
        ZonedDateTime var1;
        ZonedDateTime var3 = (var1 = ZonedDateTime.now(var0)).toLocalDate().plusDays(1L).atStartOfDay(var0);
        return Duration.between(var1, var3);
    }

    public static Long parseTime(String var0) {
        if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
            return null;
        } else {
            int var1 = var0.length();
            StringBuilder var2 = new StringBuilder(10);
            StringBuilder var3 = new StringBuilder(7);
            boolean var4 = true;

            for(int var5 = 0; var5 < var1; ++var5) {
                char var6;
                if ((var6 = var0.charAt(var5)) != ' ') {
                    if (var4) {
                        if (Strings.isEnglishDigit(var6)) {
                            var2.append(var6);
                        } else {
                            if (var2.isEmpty()) {
                                return null;
                            }

                            var4 = false;
                            var3.append((char)(var6 | 32));
                        }
                    } else {
                        var3.append((char)(var6 | 32));
                    }
                }
            }

            Integer var7;
            if ((var7 = MathUtils.parseIntUnchecked(var2, false)) != null && var7 > 0) {
                if (var3.length() <= 0) {
                    return null;
                } else {
                    Function<Long, Long> var8;
                    if ((var8 = a.get(var3.toString())) == null) {
                        return null;
                    } else {
                        return var8.apply(var7.longValue());
                    }
                }
            } else {
                return 0L;
            }
        }
    }

    public static long millisToTicks(long var0) {
        return var0 / 50L;
    }

    public static long toTicks(Duration var0) {
        return millisToTicks(var0.toMillis());
    }

    static {
        DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss").withZone(TIME_ZONE);
        DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(TIME_ZONE);
        TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss").withZone(TIME_ZONE);
        Arrays.asList("year", "years").forEach((var0) -> {
            a.put(var0, (var0x) -> TimeUnit.DAYS.toMillis(var0x * 365L));
        });
        Arrays.asList("month", "months").forEach((var0) -> {
            a.put(var0, (var0x) -> TimeUnit.DAYS.toMillis(var0x * 30L));
        });
        Arrays.asList("week", "weeks").forEach((var0) -> {
            a.put(var0, (var0x) -> TimeUnit.DAYS.toMillis(var0x * 7L));
        });
        Arrays.asList("d", "day", "days").forEach((var0) -> {
            TimeUnit var10002 = TimeUnit.DAYS;
            Objects.requireNonNull(var10002);
            a.put(var0, var10002::toMillis);
        });
        Arrays.asList("h", "hr", "hrs", "hour", "hours").forEach((var0) -> {
            TimeUnit var10002 = TimeUnit.HOURS;
            Objects.requireNonNull(var10002);
            a.put(var0, var10002::toMillis);
        });
        Arrays.asList("m", "min", "mins", "minute", "minutes").forEach((var0) -> {
            TimeUnit var10002 = TimeUnit.MINUTES;
            Objects.requireNonNull(var10002);
            a.put(var0, var10002::toMillis);
        });
        Arrays.asList("s", "sec", "secs", "second", "seconds").forEach((var0) -> {
            TimeUnit var10002 = TimeUnit.SECONDS;
            Objects.requireNonNull(var10002);
            a.put(var0, var10002::toMillis);
        });
        Arrays.asList("ms", "millisec", "millisecs", "millisecond", "milliseconds").forEach((var0) -> {
            TimeUnit var10002 = TimeUnit.MILLISECONDS;
            Objects.requireNonNull(var10002);
            a.put(var0, var10002::toMillis);
        });
    }
}

