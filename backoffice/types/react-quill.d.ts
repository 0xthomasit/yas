declare module 'react-quill' {
  import { Component } from 'react';

  export interface ReactQuillProps {
    className?: string;
    defaultValue?: string;
    value?: string;
    onChange?: (value: string) => void;
    placeholder?: string;
    readOnly?: boolean;
    theme?: string;
    modules?: Record<string, unknown>;
    formats?: string[];
    bounds?: string | HTMLElement;
    scrollingContainer?: string | HTMLElement;
    preserveWhitespace?: boolean;
  }

  export default class ReactQuill extends Component<ReactQuillProps> {}
}
