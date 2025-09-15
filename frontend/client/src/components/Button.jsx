import React from "react";

const Button = ({ msg }) => {
  return (
    <div className="group shadow-xl inline-flex gap-1.5 items-center rounded-full px-4 py-2 cursor-pointer bg-gradient-to-b from-blue-700 via-blue-500 to-blue-400  border-b-2 text-white transition active:scale-95">
      <span>{msg}</span>
      <i className="bi bi-arrow-right  transition-transform duration-300 group-hover:translate-x-1"></i>
    </div>
  );
};

export default Button;
