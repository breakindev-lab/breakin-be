package dev.breakin.openai.base;

import java.util.List;

public interface ListGptRunner<T> {
    List<T> runAsList(GptParams params);
}