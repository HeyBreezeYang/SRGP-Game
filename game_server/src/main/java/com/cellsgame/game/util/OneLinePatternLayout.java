
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package com.cellsgame.game.util;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

/**
 * A flexible layout configurable with pattern string.
 * <p>
 * The goal of this class is to {@link Layout#toByteArray format} a {@link LogEvent} and
 * return the results. The format of the result depends on the <em>conversion pattern</em>.
 * </p>
 * <p>
 * The conversion pattern is closely related to the conversion pattern of the printf function in C. A conversion pattern
 * is composed of literal text and format control expressions called <em>conversion specifiers</em>.
 * </p>
 * <p>
 * See the Log4j Manual for details on the supported pattern converters.
 * </p>
 */
@Plugin(name = "OneLinePatternLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class OneLinePatternLayout extends AbstractStringLayout {

    /**
     * Default pattern string for log output. Currently set to the
     * string <b>"%m%n"</b> which just prints the application supplied
     * message.
     */
    public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
    /**
     * Key to identify pattern converters.
     */
    public static final String KEY = "Converter";
    private static final long serialVersionUID = 1L;
    private static final String LF = "\n";
    /**
     * Initial converter for pattern.
     */
    private final List<PatternFormatter> formatters;
    /**
     * Conversion pattern.
     */
    private final String conversionPattern;
    /**
     * The current Configuration.
     */
    private final Configuration config;
    private final RegexReplacement replace;
    private final boolean alwaysWriteExceptions;
    private final boolean noConsoleNoAnsi;
    private final boolean includeNewLine;


    /**
     * Constructs a EnhancedPatternLayout using the supplied conversion pattern.
     *
     * @param config                The Configuration.
     * @param replace               The regular cab to match.
     * @param pattern               conversion pattern.
     * @param charset               The character set.
     * @param alwaysWriteExceptions Whether or not exceptions should always be handled in this pattern (if {@code true},
     *                              exceptions will be written even if the pattern does not specify so).
     * @param noConsoleNoAnsi       If {@code "true"} (default) and {@link System#console()} is null, do not output ANSI escape codes
     */
    private OneLinePatternLayout(final Configuration config, final RegexReplacement replace, final String pattern,
                                 final Charset charset, final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi,
                                 final String header, final String footer, final boolean includeNewLine) {
        super(charset, toBytes(header, charset), toBytes(footer, charset));
        this.replace = replace;
        this.conversionPattern = pattern;
        this.config = config;
        this.alwaysWriteExceptions = alwaysWriteExceptions;
        this.noConsoleNoAnsi = noConsoleNoAnsi;
        final PatternParser parser = createPatternParser(config);
        this.formatters = parser.parse(pattern == null ? DEFAULT_CONVERSION_PATTERN : pattern, this.alwaysWriteExceptions, this.noConsoleNoAnsi);
        this.includeNewLine = includeNewLine;
    }

    private static byte[] toBytes(final String str, final Charset charset) {
        if (str != null) {
            return str.getBytes(charset != null ? charset : Charset.defaultCharset());
        }
        return null;
    }

    /**
     * Create a PatternParser.
     *
     * @param config The Configuration.
     * @return The PatternParser.
     */
    public static PatternParser createPatternParser(final Configuration config) {
        if (config == null) {
            return new PatternParser(null, KEY, LogEventPatternConverter.class);
        }
        PatternParser parser = config.getComponent(KEY);
        if (parser == null) {
            parser = new PatternParser(config, KEY, LogEventPatternConverter.class);
            config.addComponent(KEY, parser);
            parser = config.getComponent(KEY);
        }
        return parser;
    }

    /**
     * Create a pattern layout.
     *
     * @param pattern               The pattern. If not specified, defaults to DEFAULT_CONVERSION_PATTERN.
     * @param config                The Configuration. Some Converters require access to the Interpolator.
     * @param replace               A Regex replacement String.
     * @param charset               The character set.
     * @param alwaysWriteExceptions If {@code "true"} (default) exceptions are always written even if the pattern contains no exception tokens.
     * @param noConsoleNoAnsi       If {@code "true"} (default is false) and {@link System#console()} is null, do not output ANSI escape codes
     * @param header                The footer to place at the top of the document, once.
     * @param footer                The footer to place at the bottom of the document, once.
     * @return The PatternLayout.
     */
    @PluginFactory
    public static OneLinePatternLayout createLayout(
            @PluginAttribute(value = "pattern", defaultString = DEFAULT_CONVERSION_PATTERN) final String pattern,
            @PluginConfiguration final Configuration config,
            @PluginElement("Replace") final RegexReplacement replace,
            @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset,
            @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) final boolean alwaysWriteExceptions,
            @PluginAttribute(value = "noConsoleNoAnsi", defaultBoolean = false) final boolean noConsoleNoAnsi,
            @PluginAttribute("header") final String header,
            @PluginAttribute("footer") final String footer,
            @PluginAttribute("includeNewLine") final boolean includeNewLine) {
        return newBuilder()
                .withPattern(pattern)
                .withConfiguration(config)
                .withRegexReplacement(replace)
                .withCharset(charset)
                .withAlwaysWriteExceptions(alwaysWriteExceptions)
                .withNoConsoleNoAnsi(noConsoleNoAnsi)
                .withHeader(header)
                .withFooter(footer)
                .withIncludeNewLine(includeNewLine)
                .build();
    }

    /**
     * Creates a PatternLayout using the default options. These options include using UTF-8, the default conversion
     * pattern, exceptions being written, and with ANSI escape codes.
     *
     * @return the PatternLayout.
     * @see #DEFAULT_CONVERSION_PATTERN Default conversion pattern
     */
    public static OneLinePatternLayout createDefaultLayout() {
        return newBuilder().build();
    }

    /**
     * Creates a builder for a custom PatternLayout.
     *
     * @return a PatternLayout builder.
     */
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    private byte[] strSubstitutorReplace(final byte... b) {
        if (b != null && config != null) {
            return getBytes(config.getStrSubstitutor().replace(new String(b, getCharset())));
        }
        return b;
    }

    @Override
    public byte[] getHeader() {
        return strSubstitutorReplace(super.getHeader());
    }

    @Override
    public byte[] getFooter() {
        return strSubstitutorReplace(super.getFooter());
    }

    /**
     * Gets the conversion pattern.
     *
     * @return the conversion pattern.
     */
    public String getConversionPattern() {
        return conversionPattern;
    }

    /**
     * Gets this PatternLayout's content format. Specified by:
     * <ul>
     * <li>Key: "structured" Value: "false"</li>
     * <li>Key: "formatType" Value: "conversion" (format uses the keywords supported by OptionConverter)</li>
     * <li>Key: "format" Value: provided "conversionPattern" param</li>
     * </ul>
     *
     * @return Map of content format keys supporting PatternLayout
     */
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "false");
        result.put("formatType", "conversion");
        result.put("format", conversionPattern);
        return result;
    }

    /**
     * Formats a logging event to a writer.
     *
     * @param event logging event to be formatted.
     * @return The event formatted as a String.
     */
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buf = new StringBuilder();
        for (final PatternFormatter formatter : formatters) {
            formatter.format(event, buf);
        }
        String str = buf.toString();
        if (replace != null) {
            str = replace.format(str);
        }
        if (includeNewLine) {
            str += LF;
        }
        return str;
    }

    @Override
    public String toString() {
        return conversionPattern;
    }

    /**
     * Custom PatternLayout builder. Use the {@link OneLinePatternLayout#newBuilder() builder factory method} to create this.
     */
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<OneLinePatternLayout> {

        // FIXME: it seems rather redundant to repeat default values (same goes for field names)
        // perhaps introduce a @PluginBuilderAttribute that has no values of its own and uses reflection?

        @PluginBuilderAttribute
        private String pattern = OneLinePatternLayout.DEFAULT_CONVERSION_PATTERN;

        @PluginConfiguration
        private Configuration configuration = null;

        @PluginElement("Replace")
        private RegexReplacement regexReplacement = null;

        // LOG4J2-783 use platform default by default
        @PluginBuilderAttribute
        private Charset charset = Charset.defaultCharset();

        @PluginBuilderAttribute
        private boolean alwaysWriteExceptions = true;

        @PluginBuilderAttribute
        private boolean noConsoleNoAnsi = false;

        @PluginBuilderAttribute
        private String header = null;

        @PluginBuilderAttribute
        private String footer = null;
        @PluginBuilderAttribute
        private boolean includeNewLine = false;

        private Builder() {
        }

        // TODO: move javadocs from PluginFactory to here

        public Builder withPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }


        public Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withRegexReplacement(final RegexReplacement regexReplacement) {
            this.regexReplacement = regexReplacement;
            return this;
        }

        public Builder withCharset(final Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder withAlwaysWriteExceptions(final boolean alwaysWriteExceptions) {
            this.alwaysWriteExceptions = alwaysWriteExceptions;
            return this;
        }

        public Builder withNoConsoleNoAnsi(final boolean noConsoleNoAnsi) {
            this.noConsoleNoAnsi = noConsoleNoAnsi;
            return this;
        }

        public Builder withHeader(final String header) {
            this.header = header;
            return this;
        }

        public Builder withFooter(final String footer) {
            this.footer = footer;
            return this;
        }

        public Builder withIncludeNewLine(final boolean includeNewLine) {
            this.includeNewLine = includeNewLine;
            return this;
        }

        @Override
        public OneLinePatternLayout build() {
            // fall back to DefaultConfiguration
            if (configuration == null) {
                configuration = new DefaultConfiguration();
            }
            return new OneLinePatternLayout(configuration, regexReplacement, pattern, charset, alwaysWriteExceptions,
                    noConsoleNoAnsi, header, footer, includeNewLine);
        }
    }
}
