package values;

import java.util.Arrays;
import java.util.LinkedList;

public class EmailMessage {

    private String from;
    private LinkedList<String> to;
    private String subject;
    private String content;
    private String mimeType;
    private LinkedList<String> cc;
    private LinkedList<String> bcc;

    private EmailMessage(String from, LinkedList<String> to, String subject, String content, String mimeType, LinkedList<String> cc, LinkedList<String> bcc) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.mimeType = mimeType;
        this.cc = cc;
        this.bcc = bcc;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String from;
        private LinkedList<String> to;
        private String subject;
        private String content;
        private String mimeType;
        private LinkedList<String> cc;
        private LinkedList<String> bcc;

        public Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder setTo(String... to) {
            if (to.length != 0) {
                this.to = new LinkedList<>(Arrays.asList(to));
            }
            return this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder setCc(String... cc) {
            if (cc.length != 0) {
                this.cc = new LinkedList<>(Arrays.asList(cc));
            }
            return this;
        }

        public Builder setBcc(String... bcc) {
            if (bcc.length != 0) {
                this.cc = new LinkedList<>(Arrays.asList(bcc));
            }
            return this;
        }

        public EmailMessage build() {
            return new EmailMessage(from, to, subject, content, mimeType, cc, bcc);
        }
    }

    public String getFrom() {
        return from;
    }

    public LinkedList<String> getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getMimeType() {
        return mimeType;
    }

    public LinkedList<String> getCc() {
        return cc;
    }

    public LinkedList<String> getBcc() {
        return bcc;
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", cc=" + cc +
                ", bcc=" + bcc +
                '}';
    }

    public static void main(String[] args) {
        EmailMessage emailMessage = EmailMessage.builder()
                .setFrom("maciek@hehe.pl")
                .setTo("kaamil", "asia", "jacek")
                .setSubject("builder pattern")
                .setContent("lolololololo")
                .build();
        System.out.println(emailMessage);
    }
}
