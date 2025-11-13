import { $, on } from "../core/dom.js";
import { loadUserId } from "../core/storage.js";
import { PostsAPI } from "../api/posts.js";

function escapeHtml(str = "") {
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function createPostElement(post) {
  const { id, title, authorNickname, likes, views, createdAt, commentsCount } =
    post;

  const article = document.createElement("article");
  article.className = "post";
  article.dataset.postId = id;

  article.innerHTML = `
    <div class="post-head">
      <h2 class="post-title">${escapeHtml(title)}</h2>
      <time class="post-date">${escapeHtml(createdAt || "")}</time>
    </div>

    <div class="post-meta">
      <span>좋아요 ${likes ?? 0}</span>
      <span>댓글 ${commentsCount ?? 0}</span>
      <span>조회수 ${views ?? 0}</span>
    </div>

    <div class="post-divider"></div>

    <footer class="post-footer">
      <span class="author-avatar" aria-hidden="true"></span>
      <span class="author-name">${escapeHtml(authorNickname || "익명")}</span>
    </footer>
  `;

  return article;
}

async function loadPosts() {
  const boardEl = $(".board");
  if (!boardEl) return;

  boardEl.innerHTML = "";

  try {
    const pageData = await PostsAPI.getList({
      page: 0,
      limit: 10,
      sort: "DATE",
    });

    const list = pageData?.content ?? pageData ?? [];

    if (!Array.isArray(list) || list.length === 0) {
      const empty = document.createElement("p");
      empty.textContent = "아직 작성된 게시글이 없습니다.";
      empty.className = "empty";
      boardEl.appendChild(empty);
      return;
    }

    list.forEach((post) => {
      const card = createPostElement(post);
      boardEl.appendChild(card);
    });
  } catch (err) {
    console.error("게시글 목록 조회 실패:", err);
    const errorMsg = document.createElement("p");
    errorMsg.textContent =
      "게시글을 불러오는 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    errorMsg.className = "empty";
    $(".board")?.appendChild(errorMsg);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const writeBtn = $(".intro .btn.primary");
  const avatarBtn = $(".site-header .avatar");
  const boardEl = $(".board");

  loadPosts();

  if (writeBtn) {
    on(writeBtn, "click", () => {
      const userId = loadUserId();
      if (!userId) {
        alert("게시글 작성은 로그인 후 이용 가능합니다.");
        window.location.href = "./login.html";
        return;
      }
      window.location.href = "./post-create.html";
    });
  }

  if (avatarBtn) {
    on(avatarBtn, "click", () => {
      const userId = loadUserId();
      if (!userId) {
        window.location.href = "./login.html";
      } else {
        window.location.href = "./profile-edit.html";
      }
    });
  }

  if (boardEl) {
    on(boardEl, "click", (e) => {
      const postEl = e.target.closest(".post");
      if (!postEl) return;

      const postId = postEl.dataset.postId;
      if (!postId) return;

      window.location.href = `./post-detail.html?postId=${postId}`;
    });
  }
});
