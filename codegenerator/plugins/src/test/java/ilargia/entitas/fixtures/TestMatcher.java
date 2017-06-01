package ilargia.entitas.fixtures;

import ilargia.entitas.matcher.Matcher;

public class TestMatcher {

    private static Matcher _matcherInteractive;
    private static Matcher _matcherMotion;
    private static Matcher _matcherPlayer;
    private static Matcher _matcherPosition;
    private static Matcher _matcherView;
    private static Matcher _matcherSize;

    public static Matcher Interactive() {
        if (_matcherInteractive == null) {
            Matcher matcher = (Matcher) Matcher
                    .AllOf(TestComponentIds.Interactive);
            matcher.componentNames = TestComponentIds.componentNames();
            _matcherInteractive = matcher;
        }
        return _matcherInteractive;
    }

    public static Matcher Motion() {
        if (_matcherMotion == null) {
            Matcher matcher = (Matcher) Matcher.AllOf(TestComponentIds.Motion);
            matcher.componentNames = TestComponentIds.componentNames();
            _matcherMotion = matcher;
        }
        return _matcherMotion;
    }

    public static Matcher Player() {
        if (_matcherPlayer == null) {
            Matcher matcher = (Matcher) Matcher.AllOf(TestComponentIds.Player);
            matcher.componentNames = TestComponentIds.componentNames();
            _matcherPlayer = matcher;
        }
        return _matcherPlayer;
    }

    public static Matcher Position() {
        if (_matcherPosition == null) {
            Matcher matcher = (Matcher) Matcher
                    .AllOf(TestComponentIds.Position);
            matcher.componentNames = TestComponentIds.componentNames();
            _matcherPosition = matcher;
        }
        return _matcherPosition;
    }

    public static Matcher View() {
        if (_matcherView == null) {
            Matcher matcher = (Matcher) Matcher.AllOf(TestComponentIds.View);
            matcher.componentNames = TestComponentIds.componentNames();
            _matcherView = matcher;
        }
        return _matcherView;
    }

    public static Matcher Size() {
        if (_matcherSize == null) {
            Matcher matcher = (Matcher) Matcher.AllOf(TestComponentIds.Size);
            matcher.componentNames = TestComponentIds.componentNames();
            _matcherSize = matcher;
        }
        return _matcherSize;
    }
}