package com.hs.languagelearningapi.email;


import java.util.Map;

record Email(String to, String from, String subject, String text, String template,
             Map<String, Object> properties) {
    public static Builder builder() {
        return new Builder();
    }
    public static final class Builder {
        private String to;
        private String from;
        private String subject;
        private String text;
        private String template;
        private Map<String, Object> properties;

        private Builder() {
        }

        public Builder to(String val) {
            to = val;
            return this;
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder subject(String val) {
            subject = val;
            return this;
        }

        public Builder text(String val) {
            text = val;
            return this;
        }

        public Builder template(String val) {
            template = val;
            return this;
        }

        public Builder properties(Map<String, Object> val) {
            properties = val;
            return this;
        }

        public Email build() {
            return new Email(to, from, subject, text, template, properties);
        }
    }
}
