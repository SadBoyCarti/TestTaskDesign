package com.ekazhev.said;

import java.time.LocalDateTime;
import java.util.List;

public interface DatesToCronConverter {
    String convert(List<LocalDateTime> list);
    String getImplementationInfo();
}
