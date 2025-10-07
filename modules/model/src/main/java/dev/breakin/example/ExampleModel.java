package dev.breakin.example;

import dev.breakin.AuditProps;

public interface ExampleModel extends AuditProps {
    Long getExampleId();
    String getName();
}
