import React, { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import Sidebar from "./Sidebar";
import "./CSS/DashboardPage.css";
import { Server, CheckSquare, Globe, Clock } from "react-feather";
import { useBooksCount } from "../../utilities/BorrowReturnCount";

import Chart from "chart.js/auto";

export default function DashboardPage() {
  const { borrowed, returned } = useBooksCount();
  const chartRef = useRef(null);
  const chartInstanceRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!chartRef.current) return;

    if (chartInstanceRef.current) {
      chartInstanceRef.current.destroy();
    }

    const ctx = chartRef.current.getContext("2d");
    chartInstanceRef.current = new Chart(ctx, {
      type: "doughnut",
      data: {
        labels: ["Total Borrowed Books", "Total Returned Books"],
        datasets: [
          {
            data: [borrowed, returned],
            borderColor: ["#0E79B2", "#1abc9c"],
            backgroundColor: ["#0E79B2", "transparent"],
            hoverBackgroundColor: ["#0E79B2", "#1abc9c"],
            hoverBorderColor: ["transparent", "transparent"],
            borderWidth: [3, 3],
            hoverOffset: 10,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: "bottom",
            labels: {
              color: "#333",
              font: {
                size: 14,
              },
            },
          },
        },
      },
    });

    return () => {
      if (chartInstanceRef.current) {
        chartInstanceRef.current.destroy();
      }
    };
  }, [borrowed, returned]);

  return (
    <div className="appLayout">
      <Sidebar />
      <div className="main">
        <Navbar />
        <div className="contentRow">
          <div className="left-column">
            <div className="button-grid">
              <button
                className="custom-button bb"
                onClick={() => navigate("/borrowed-books")}
              >
                <Server />
                Your Borrowed Book List
              </button>
              <button
                className="custom-button rb"
                onClick={() => navigate("/returned")}
              >
                <CheckSquare />
                Your Returned Book List
              </button>
              <button
                className="custom-button cb"
                onClick={() => navigate("/catalogue")}
              >
                <Globe />
                Let's Browse the Inventory
              </button>
              <button
                className="custom-button overdue-button"
                onClick={() => navigate("/overdue")}
              >
                <Clock />
                Your Overdue Books
              </button>
            </div>
            <div className="quote-container">
              <p className="quote-text">
                "Reading gives us someplace to go when we have to stay where we
                are."
              </p>
              <p className="quote-author">- Mason Cooley</p>
            </div>
          </div>
          <div className="right-column">
            <div className="piechart-container">
              <canvas ref={chartRef} id="dashboardPieChart"></canvas>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
