function FeedbackBanner({ feedback, onClear }) {
  return (
    <section className={`feedback ${feedback.type}`}>
      <p>{feedback.message}</p>
      <button type="button" onClick={onClear}>
        Fechar
      </button>
    </section>
  );
}

export default FeedbackBanner;
