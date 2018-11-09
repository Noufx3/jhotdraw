/* @(#)QualifiedName.java
 * Copyright © 2018 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class QualifiedName implements Comparable<QualifiedName> {
    private final @Nullable String namespace;
    private final @Nonnull String name;

    public QualifiedName(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    @Nullable
    public String getNamespace() {
        return namespace;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QualifiedName)) {
            return false;
        }
        QualifiedName that = (QualifiedName) o;
        return Objects.equals(namespace, that.namespace) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name);
    }

    @Override
    public int compareTo(@NotNull QualifiedName o) {
        return this.name.compareTo(o.name);
    }
}