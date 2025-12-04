type Props = {
  show: boolean;
};

export default function SpinnerComponent({ show }: Props) {
  return (
    <div className="spinner" hidden={!show}>
      <div className="spinner-icon" />
    </div>
  );
}
