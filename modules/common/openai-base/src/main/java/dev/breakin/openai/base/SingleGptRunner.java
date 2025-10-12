package dev.breakin.openai.base;

public interface SingleGptRunner<T> {

    T run(GptParams params);

}
